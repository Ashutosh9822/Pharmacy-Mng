package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PharmacyProfileRequest;
import com.becoder.dto.PharmacyProfileResponse;
import com.becoder.service.PharmacyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pharmacy/profile")
@PreAuthorize("hasRole('ADMIN')")
public class PharmacyController {

	@Autowired
	private PharmacyService pharmacyService;

	// Get pharmacy profile
	@GetMapping
	public ResponseEntity<PharmacyProfileResponse> getPharmacyProfile() {
		PharmacyProfileResponse response = pharmacyService.getPharmacyProfile();
		return ResponseEntity.ok(response);
	}

	// Update pharmacy profile
	@PutMapping
	public ResponseEntity<PharmacyProfileResponse> updatePharmacyProfile(@Valid @RequestBody PharmacyProfileRequest request) {
		PharmacyProfileResponse response = pharmacyService.updatePharmacyProfile(request);
		return ResponseEntity.ok(response);
	}
}
