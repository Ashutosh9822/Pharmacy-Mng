
package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.AlertSummaryResponse;
import com.becoder.service.AlertSummaryService;
import com.becoder.service.AlertValidationService;

@RestController
@RequestMapping("/api/v1/alerts/summary")
public class AlertSummaryController {

	@Autowired
	private AlertSummaryService alertSummaryService;

	@Autowired
	private AlertValidationService alertValidationService;

	@GetMapping
	public AlertSummaryResponse getAlertSummary(@RequestParam(defaultValue = "30") int expiryDays,
			@RequestParam(defaultValue = "10") int lowStockThreshold,
			@RequestParam(defaultValue = "50") int targetStock) {

		alertValidationService.validateAlertParameters(expiryDays, lowStockThreshold, targetStock);

		return alertSummaryService.getAlertSummary(expiryDays, lowStockThreshold, targetStock);
	}
}
