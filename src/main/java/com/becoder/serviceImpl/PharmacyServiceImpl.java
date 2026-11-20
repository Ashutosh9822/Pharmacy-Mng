
package com.becoder.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.PharmacyProfileRequest;
import com.becoder.dto.PharmacyProfileResponse;
import com.becoder.entity.Pharmacy;
import com.becoder.entity.User;
import com.becoder.repository.PharmacyRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.PharmacyService;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public PharmacyProfileResponse getPharmacyProfile() {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException(
                    "Only administrators can access pharmacy settings");
        }

        Pharmacy pharmacy = currentUser.getPharmacy();

        if (pharmacy == null || !pharmacy.isActive()) {
            throw new IllegalArgumentException(
                    "Active pharmacy not found");
        }

        return mapToResponse(pharmacy);
    }

    @Override
    @Transactional
    public PharmacyProfileResponse updatePharmacyProfile(
            PharmacyProfileRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new AccessDeniedException(
                    "Only administrators can update pharmacy settings");
        }

        Pharmacy pharmacy = currentUser.getPharmacy();

        if (pharmacy == null || !pharmacy.isActive()) {
            throw new IllegalArgumentException(
                    "Active pharmacy not found");
        }

        String email = request.getEmail().trim().toLowerCase();

        boolean emailExists =
                pharmacyRepository.existsByEmailIgnoreCase(email);

        if (emailExists &&
                !pharmacy.getEmail().equalsIgnoreCase(email)) {

            throw new IllegalArgumentException(
                    "This email is already registered with another pharmacy");
        }

        pharmacy.setPharmacyName(request.getPharmacyName().trim());
        pharmacy.setEmail(email);

        pharmacy.setPhone(
                request.getPhone() == null ||
                request.getPhone().isBlank()
                ? null
                : request.getPhone().trim());

        pharmacy.setAddress(
                request.getAddress() == null ||
                request.getAddress().isBlank()
                ? null
                : request.getAddress().trim());

        Pharmacy updatedPharmacy =
                pharmacyRepository.save(pharmacy);

        return mapToResponse(updatedPharmacy);
    }

    private PharmacyProfileResponse mapToResponse(Pharmacy pharmacy) {

        return new PharmacyProfileResponse(
                pharmacy.getId(),
                pharmacy.getPharmacyName(),
                pharmacy.getEmail(),
                pharmacy.getPhone(),
                pharmacy.getAddress(),
                pharmacy.isActive(),
                pharmacy.getCreatedOn(),
                pharmacy.getUpdatedOn()
        );
    }
}
