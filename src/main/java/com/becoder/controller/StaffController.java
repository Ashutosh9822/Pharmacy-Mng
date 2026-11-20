package com.becoder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.StaffRequest;
import com.becoder.dto.StaffResponse;
import com.becoder.dto.StaffUpdateRequest;
import com.becoder.service.StaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

	private final StaffService staffService;

	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}

	// CREATE STAFF
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<StaffResponse> createStaff(@Valid @RequestBody StaffRequest request) {
		StaffResponse response = staffService.createStaff(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// GET ALL STAFF
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<StaffResponse>> getStaffMembers() {
		List<StaffResponse> staffMembers = staffService.getStaffMembers();
		return ResponseEntity.ok(staffMembers);
	}

	// UPDATE STAFF
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<StaffResponse> updateStaff(@PathVariable Long id,@Valid @RequestBody StaffUpdateRequest request) {
		StaffResponse response = staffService.updateStaff(id, request);
		return ResponseEntity.ok(response);
	}

	// DELETE STAFF (SOFT DEACTIVATION)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
		staffService.deleteStaff(id);
		return ResponseEntity.noContent().build();
	}
}
