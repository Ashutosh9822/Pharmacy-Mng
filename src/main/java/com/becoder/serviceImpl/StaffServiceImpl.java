package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.StaffRequest;
import com.becoder.dto.StaffResponse;
import com.becoder.dto.StaffUpdateRequest;
import com.becoder.entity.User;
import com.becoder.repository.UserRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    @Transactional
    public StaffResponse createStaff(StaffRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException(
                    "Only pharmacy administrators can add staff");
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists");
        }

        User staff = new User();

        staff.setFullName(request.getFullName().trim());
        staff.setEmail(email);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole("USER");
        staff.setActive(true);
        staff.setPharmacy(currentUser.getPharmacy());

        // New staff contact details
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setAddress(request.getAddress());

        User savedStaff = userRepository.save(staff);

        return mapToResponse(savedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponse> getStaffMembers() {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException(
                    "Only pharmacy administrators can view staff members");
        }

        Long pharmacyId = currentUser.getPharmacy().getId();

        return userRepository.findByPharmacyId(pharmacyId)
                .stream()
                .filter(user -> "USER".equals(user.getRole()))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(Long staffId, StaffUpdateRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException(
                    "Only pharmacy administrators can update staff members");
        }

        Long pharmacyId = currentUser.getPharmacy().getId();

        User staff = userRepository
                .findByIdAndPharmacyId(staffId, pharmacyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Staff member not found"));

        if (!"USER".equals(staff.getRole())) {
            throw new AccessDeniedException(
                    "You cannot update an administrator account");
        }

        if (!staff.isActive()) {
            throw new IllegalArgumentException(
                    "Inactive staff members cannot be updated");
        }

        String email = request.getEmail().trim().toLowerCase();

        User existingUser = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        if (existingUser != null && !existingUser.getId().equals(staffId)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        staff.setFullName(request.getFullName().trim());
        staff.setEmail(email);

        // Update staff contact details
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setAddress(request.getAddress());

        User updatedStaff = userRepository.save(staff);

        return mapToResponse(updatedStaff);
    }

    @Override
    @Transactional
    public void deleteStaff(Long staffId) {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException("Only pharmacy administrators can delete staff members");
        }

        Long pharmacyId = currentUser.getPharmacy().getId();

        User staff = userRepository
                .findByIdAndPharmacyId(staffId, pharmacyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Staff member not found"));

        if (!"USER".equals(staff.getRole())) {
            throw new AccessDeniedException("You cannot deactivate an administrator account");
        }

        if (!staff.isActive()) {
            throw new IllegalArgumentException("Staff member is already inactive");
        }

        // Soft delete: preserve the account and its historical records.
        staff.setActive(false);

        userRepository.save(staff);
    }

    private StaffResponse mapToResponse(User user) {

        return new StaffResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getPharmacy().getId(),
                user.getPhoneNumber(),
                user.getAddress()
        );
    }
}
