
package com.becoder.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.AuthResponse;
import com.becoder.dto.LoginRequest;
import com.becoder.dto.RegisterRequest;
import com.becoder.entity.Pharmacy;
import com.becoder.entity.User;
import com.becoder.repository.PharmacyRepository;
import com.becoder.repository.UserRepository;
import com.becoder.service.AuthService;
import com.becoder.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PharmacyRepository pharmacyRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Override
	@Transactional
	public AuthResponse register(RegisterRequest request) {

		String email = request.getEmail().trim().toLowerCase();
		String pharmacyEmail = request.getPharmacyEmail().trim().toLowerCase();

		// Check whether owner email already exists
		if (userRepository.existsByEmailIgnoreCase(email)) {
			throw new IllegalArgumentException("An account with this email already exists");
		}

		// Check whether pharmacy email already exists
		if (pharmacyRepository.existsByEmailIgnoreCase(pharmacyEmail)) {
			throw new IllegalArgumentException("A pharmacy with this email already exists");
		}

		// Create pharmacy
		Pharmacy pharmacy = new Pharmacy();

		pharmacy.setPharmacyName(request.getPharmacyName().trim());
		pharmacy.setEmail(pharmacyEmail);
		pharmacy.setPhone(request.getPharmacyPhone() == null ? null : request.getPharmacyPhone().trim());
		pharmacy.setAddress(request.getPharmacyAddress() == null ? null : request.getPharmacyAddress().trim());
		pharmacy.setActive(true);

		Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

		// Create pharmacy owner
		User user = new User();

		user.setFullName(request.getFullName().trim());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// Every new pharmacy owner automatically becomes ADMIN
		user.setRole("ADMIN");
		user.setActive(true);

		// Associate owner with the newly created pharmacy
		user.setPharmacy(savedPharmacy);

		User savedUser = userRepository.save(user);

		// Generate JWT
		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(savedUser.getEmail())
				.password(savedUser.getPassword()).roles(savedUser.getRole()).build();

		String token = jwtService.generateToken(userDetails);

		return new AuthResponse("Registration successful", token, savedUser.getId(), savedUser.getFullName(),
				savedUser.getEmail(), savedUser.getRole());
	}

	@Override
	public AuthResponse login(LoginRequest request) {

		String email = request.getEmail().trim().toLowerCase();

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

		String token = jwtService.generateToken(userDetails);

		return new AuthResponse("Login successful", token, user.getId(), user.getFullName(), user.getEmail(),
				user.getRole());
	}
}
