
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.ExpiryBatchResponse;
import com.becoder.service.ExpiryTrackingService;

@RestController
@RequestMapping("/api/v1/expiry")
public class ExpiryTrackingController {

	@Autowired
	private ExpiryTrackingService expiryTrackingService;

	// Get all batches that have already expired
	@GetMapping("/expired")
	public ResponseEntity<List<ExpiryBatchResponse>> getExpiredBatches() {
		return ResponseEntity.ok(expiryTrackingService.getExpiredBatches());
	}

	// Get batches expiring within the requested number of days
	@GetMapping("/expiring-soon")
	public ResponseEntity<List<ExpiryBatchResponse>> getExpiringSoon(@RequestParam(defaultValue = "30") int days) {
		return ResponseEntity.ok(expiryTrackingService.getExpiringSoonBatches(days));
	}

	// Get expired batches and batches expiring within 90 days
	@GetMapping("/report")
	public ResponseEntity<List<ExpiryBatchResponse>> getExpiryReport() {
		return ResponseEntity.ok(expiryTrackingService.getExpiryReport());
	}
}
