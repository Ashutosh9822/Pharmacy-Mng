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

import com.becoder.dto.MedicineRequest;
import com.becoder.dto.MedicineResponse;
import com.becoder.service.MedicineService;

@RestController
@RequestMapping("/api/v1/medicines")
@Validated
public class MedicineController {

	@Autowired
	private MedicineService medicineService;

	@PostMapping("/create-medicine")
	public ResponseEntity<MedicineResponse> createMedicine(@Valid @RequestBody MedicineRequest request) {
		MedicineResponse response = medicineService.createMedicine(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/get-medicine")
	public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
		return ResponseEntity.ok(medicineService.getAllMedicines());
	}

	@GetMapping("/active")
	public ResponseEntity<List<MedicineResponse>> getActiveMedicines() {
		return ResponseEntity.ok(medicineService.getActiveMedicines());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
		return ResponseEntity.ok(medicineService.getMedicineById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id,@Valid @RequestBody MedicineRequest request) {
		return ResponseEntity.ok(medicineService.updateMedicine(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
		medicineService.deleteMedicine(id);
		return ResponseEntity.noContent().build();
	}
}
