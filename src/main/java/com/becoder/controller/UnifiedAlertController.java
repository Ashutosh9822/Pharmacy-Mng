
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.UnifiedAlertResponse;
import com.becoder.service.AlertValidationService;
import com.becoder.service.UnifiedAlertService;

@RestController
@RequestMapping("/api/v1/alerts")
public class UnifiedAlertController {

	@Autowired
	private UnifiedAlertService unifiedAlertService;

	@Autowired
	private AlertValidationService alertValidationService;

	@GetMapping
	public List<UnifiedAlertResponse> getAllAlerts(@RequestParam(defaultValue = "30") int expiryDays,
			@RequestParam(defaultValue = "10") int lowStockThreshold,
			@RequestParam(defaultValue = "50") int targetStock, @RequestParam(required = false) String severity,
			@RequestParam(required = false) String type) {

		alertValidationService.validateAlertParameters(expiryDays, lowStockThreshold, targetStock);
		alertValidationService.validateSeverity(severity);
		alertValidationService.validateAlertType(type);

		return unifiedAlertService.getAllAlerts(expiryDays, lowStockThreshold, targetStock, severity, type);
	}
}
