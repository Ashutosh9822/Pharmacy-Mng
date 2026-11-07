
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.LowStockAlertResponse;
import com.becoder.service.AlertValidationService;
import com.becoder.service.LowStockAlertService;

@RestController
@RequestMapping("/api/v1/low-stock-alerts")
public class LowStockAlertController {

	@Autowired
	private LowStockAlertService lowStockAlertService;

	@Autowired
	private AlertValidationService alertValidationService;

	@GetMapping
	public List<LowStockAlertResponse> getLowStockAlerts(@RequestParam(defaultValue = "10") int lowStockThreshold,
			@RequestParam(defaultValue = "50") int targetStock) {

		alertValidationService.validateAlertParameters(1, lowStockThreshold, targetStock);
		return lowStockAlertService.getLowStockAlerts(lowStockThreshold, targetStock);
	}
}
