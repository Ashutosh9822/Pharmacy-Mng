package com.becoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.becoder.entity.User;
import com.becoder.repository.UserRepository;

@Service
public class CurrentUserService {

	@Autowired
	private UserRepository userRepository;

	public User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {

			throw new IllegalStateException("User is not authenticated");
		}

		String email = authentication.getName();

		User user = userRepository.findByEmailIgnoreCase(email).filter(User::isActive)
				.orElseThrow(() -> new IllegalStateException("Authenticated user account is not available"));

		if (user.getPharmacy() == null || !user.getPharmacy().isActive()) {
			throw new IllegalStateException("Your pharmacy account is inactive. Please contact support.");
		}

		return user;
	}

	public Long getCurrentPharmacyId() {
		return getCurrentUser().getPharmacy().getId();
	}

	public boolean isCurrentUserAdmin() {
		return "ADMIN".equals(getCurrentUser().getRole());
	}
}
