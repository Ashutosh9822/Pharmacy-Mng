package com.becoder.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.CustomerRequest;
import com.becoder.dto.CustomerResponse;
import com.becoder.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	// Create customer
	// ADMIN and USER can create customers
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
		CustomerResponse response = customerService.createCustomer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Get all customers
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	// Get customer by ID
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
		return ResponseEntity.ok(customerService.getCustomerById(id));
	}

	// Search customers by name
	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String name) {
		return ResponseEntity.ok(customerService.searchCustomersByName(name));
	}

	// Update customer
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
			@Valid @RequestBody CustomerRequest request) {

		return ResponseEntity.ok(customerService.updateCustomer(id, request));
	}

	// Soft delete customer
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
		return ResponseEntity.noContent().build();
	}
}
