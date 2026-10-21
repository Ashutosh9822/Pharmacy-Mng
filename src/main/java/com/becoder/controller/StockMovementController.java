package com.becoder.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.becoder.dto.StockMovementRequest;
import com.becoder.dto.StockMovementResponse;
import com.becoder.service.StockMovementService;

@RestController
@RequestMapping("/api/v1/stock-movements")
public class StockMovementController {

	@Autowired
	private StockMovementService stockMovementService;

	// Receive stock into a batch
	@PostMapping("/stock-in")
	public ResponseEntity<StockMovementResponse> stockIn(@Valid @RequestBody StockMovementRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.stockIn(request));
	}

	// Dispense or sell stock from a batch
	@PostMapping("/stock-out")
	public ResponseEntity<StockMovementResponse> stockOut(@Valid @RequestBody StockMovementRequest request) {
		return ResponseEntity.ok(stockMovementService.stockOut(request));
	}

	// Get all stock movements
	@GetMapping
	public ResponseEntity<List<StockMovementResponse>> getAllMovements() {
		return ResponseEntity.ok(stockMovementService.getAllMovements());
	}

	// Get movement history for one batch
	@GetMapping("/batch/{batchId}")
	public ResponseEntity<List<StockMovementResponse>> getMovementsByBatch(@PathVariable Long batchId) {
		return ResponseEntity.ok(stockMovementService.getMovementsByBatch(batchId));
	}
}
