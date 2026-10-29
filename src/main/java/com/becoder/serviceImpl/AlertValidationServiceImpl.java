package com.becoder.serviceImpl;

import org.springframework.stereotype.Service;

import com.becoder.service.AlertValidationService;

@Service
public class AlertValidationServiceImpl implements AlertValidationService {

	@Override
	public void validateAlertParameters(int expiryDays, int lowStockThreshold, int targetStock) {

		if (expiryDays <= 0) {
			throw new IllegalArgumentException("Expiry days must be greater than zero.");
		}

		if (lowStockThreshold < 0) {
			throw new IllegalArgumentException("Low-stock threshold cannot be negative.");
		}

		if (targetStock <= lowStockThreshold) {
			throw new IllegalArgumentException("Target stock must be greater than the low-stock threshold.");
		}
	}

	@Override
	public void validateSeverity(String severity) {

		if (severity == null || severity.isBlank()) {
			return;
		}

		String normalizedSeverity = severity.trim().toUpperCase();

		if (!normalizedSeverity.equals("CRITICAL") && !normalizedSeverity.equals("HIGH")
				&& !normalizedSeverity.equals("MEDIUM") && !normalizedSeverity.equals("LOW")) {
			throw new IllegalArgumentException("Invalid severity. Allowed values: CRITICAL, HIGH, MEDIUM, LOW.");
		}
	}

	@Override
	public void validateAlertType(String type) {

		if (type == null || type.isBlank()) {
			return;
		}

		String normalizedType = type.trim().toUpperCase();

		if (!normalizedType.equals("EXPIRY") && !normalizedType.equals("LOW_STOCK")) {
			throw new IllegalArgumentException("Invalid alert type. Allowed values: EXPIRY, LOW_STOCK.");
		}
	}
}
