
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.InventoryOptimizationResponse;
import com.becoder.service.InventoryOptimizationService;

@RestController
@RequestMapping("/api/v1/inventory-optimization")
public class InventoryOptimizationController {

	@Autowired
	private InventoryOptimizationService inventoryOptimizationService;

	@GetMapping("/report")
	public ResponseEntity<List<InventoryOptimizationResponse>> getInventoryReport(
			@RequestParam(defaultValue = "10") int lowStockThreshold,
			@RequestParam(defaultValue = "50") int targetStock) {

		return ResponseEntity.ok(inventoryOptimizationService.getInventoryReport(lowStockThreshold, targetStock));
	}

	@GetMapping("/low-stock")
	public ResponseEntity<List<InventoryOptimizationResponse>> getLowStockMedicines(
			@RequestParam(defaultValue = "10") int lowStockThreshold,
			@RequestParam(defaultValue = "50") int targetStock) {
		
		return ResponseEntity.ok(inventoryOptimizationService.getLowStockMedicines(lowStockThreshold, targetStock));
	}
}
