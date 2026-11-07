
package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.StaffRequest;
import com.becoder.dto.StaffResponse;
import com.becoder.entity.User;
import com.becoder.repository.UserRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public StaffServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CurrentUserService currentUserService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public StaffResponse createStaff(StaffRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new org.springframework.security.access.AccessDeniedException("Only pharmacy administrators can add staff");
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        User staff = new User();

        staff.setFullName(request.getFullName().trim());
        staff.setEmail(email);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));

        // Staff accounts always receive USER role
        staff.setRole("USER");
        staff.setActive(true);

        // Staff automatically belong to the logged-in ADMIN's pharmacy
        staff.setPharmacy(currentUser.getPharmacy());

        User savedStaff = userRepository.save(staff);

        return mapToResponse(savedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponse> getStaffMembers() {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new org.springframework.security.access.AccessDeniedException("Only pharmacy administrators can view staff members");
        }

        Long pharmacyId = currentUser.getPharmacy().getId();

        return userRepository.findByPharmacyId(pharmacyId)
                .stream()
                .filter(user -> "USER".equals(user.getRole()))
                .map(this::mapToResponse)
                .toList();
    }

    private StaffResponse mapToResponse(User user) {

        return new StaffResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getPharmacy().getId());
    }
}
