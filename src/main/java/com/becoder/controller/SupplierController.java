
package com.becoder.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.SupplierRequest;
import com.becoder.dto.SupplierResponse;
import com.becoder.service.SupplierService;

@RestController
@RequestMapping("/api/v1/suppliers")
@Validated
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	// 1. Create supplier
	@PostMapping("/create-supplier")
	public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierRequest request) {
		SupplierResponse response = supplierService.createSupplier(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. Get all non-deleted suppliers
	@GetMapping("get-suppliers")
	public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
		return ResponseEntity.ok(supplierService.getAllSuppliers());
	}

	// 3. Get active suppliers
	@GetMapping("/active")
	public ResponseEntity<List<SupplierResponse>> getActiveSuppliers() {
		return ResponseEntity.ok(supplierService.getActiveSuppliers());
	}

	// 4. Get supplier by ID
	@GetMapping("/{id}")
	public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
		return ResponseEntity.ok(supplierService.getSupplierById(id));
	}

	// 5. Update supplier
	@PutMapping("/{id}")
	public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id,@Valid @RequestBody SupplierRequest request) {
		return ResponseEntity.ok(supplierService.updateSupplier(id, request));
	}

	// 6. Soft-delete supplier
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
		supplierService.deleteSupplier(id);
		return ResponseEntity.noContent().build();
	}
}
