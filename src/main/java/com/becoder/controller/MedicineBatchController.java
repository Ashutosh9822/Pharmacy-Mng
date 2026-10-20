package com.becoder.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.becoder.dto.MedicineBatchRequest;
import com.becoder.dto.MedicineBatchResponse;
import com.becoder.service.MedicineBatchService;

@RestController
@RequestMapping("/api/v1/medicine-batches")
public class MedicineBatchController {

	@Autowired
	private MedicineBatchService batchService;

	// Create a new medicine batch
	@PostMapping("/create-batch")
	public ResponseEntity<MedicineBatchResponse> createBatch(@Valid @RequestBody MedicineBatchRequest request) {
		MedicineBatchResponse response = batchService.createBatch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get all non-deleted batches
	@GetMapping("get-batch")
	public ResponseEntity<List<MedicineBatchResponse>> getAllBatches() {
		return ResponseEntity.ok(batchService.getAllBatches());
	}

	// Get a batch by ID
	@GetMapping("/{id}")
	public ResponseEntity<MedicineBatchResponse> getBatchById(@PathVariable Long id) {
		return ResponseEntity.ok(batchService.getBatchById(id));
	}

	// Update a batch
	@PutMapping("/{id}")
	public ResponseEntity<MedicineBatchResponse> updateBatch(@PathVariable Long id,@Valid @RequestBody MedicineBatchRequest request) {
		return ResponseEntity.ok(batchService.updateBatch(id, request));
	}

	// Soft-delete a batch
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
		batchService.deleteBatch(id);
		return ResponseEntity.noContent().build();
	}

	// Get batches belonging to a medicine
	@GetMapping("/medicine/{medicineId}")
	public ResponseEntity<List<MedicineBatchResponse>> getBatchesByMedicine(@PathVariable Long medicineId) {
		return ResponseEntity.ok(batchService.getBatchesByMedicine(medicineId));
	}

	// Get batches supplied by a supplier
	@GetMapping("/supplier/{supplierId}")
	public ResponseEntity<List<MedicineBatchResponse>> getBatchesBySupplier(@PathVariable Long supplierId) {
		return ResponseEntity.ok(batchService.getBatchesBySupplier(supplierId));
	}
	
}
