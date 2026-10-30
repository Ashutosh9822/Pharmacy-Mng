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
import com.becoder.entity.User;
import com.becoder.repository.UserRepository;
import com.becoder.service.AuthService;
import com.becoder.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;
	
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

		if (userRepository.existsByEmailIgnoreCase(email)) {
			throw new IllegalArgumentException("An account with this email already exists");
		}

		User user = new User();
		user.setFullName(request.getFullName().trim());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// Public registration always creates a normal user.
		user.setRole("USER");
		user.setActive(true);

		User savedUser = userRepository.save(user);

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
