package com.becoder.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.SaleBatchResponse;
import com.becoder.dto.SaleMedicineSearchResponse;
import com.becoder.dto.SalePreviewRequest;
import com.becoder.dto.SalePreviewResponse;
import com.becoder.dto.SaleRequest;
import com.becoder.dto.SaleResponse;
import com.becoder.service.SaleService;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

	@Autowired
	private SaleService saleService;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request) {
		SaleResponse response = saleService.createSale(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<List<SaleResponse>> getAllSales() {
		return ResponseEntity.ok(saleService.getAllSales());
	}

	@GetMapping("/{saleId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long saleId) {
		return ResponseEntity.ok(saleService.getSaleById(saleId));
	}

	@PostMapping("/{saleId}/cancel")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<SaleResponse> cancelSale(@PathVariable Long saleId) {
		return ResponseEntity.ok(saleService.cancelSale(saleId));
	}
	
	@GetMapping("/medicine-search")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<List<SaleMedicineSearchResponse>> searchMedicines(@RequestParam(defaultValue = "") String keyword) {
	    return ResponseEntity.ok(saleService.searchMedicines(keyword));
	}
	
	@GetMapping("/medicine/{medicineId}/batches")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<List<SaleBatchResponse>> getAvailableBatches(@PathVariable Long medicineId) {
	    return ResponseEntity.ok(saleService.getAvailableBatches(medicineId));
	}
	
	@PostMapping("/preview")
	public ResponseEntity<SalePreviewResponse> previewSale(@Valid @RequestBody SalePreviewRequest request) {
	    SalePreviewResponse response = saleService.previewSale(request);
	    return ResponseEntity.ok(response);
	}
	
}
