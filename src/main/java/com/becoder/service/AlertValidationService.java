package com.becoder.service;

public interface AlertValidationService {

	public void validateAlertParameters(int expiryDays, int lowStockThreshold, int targetStock);

	public void validateSeverity(String severity);

	public void validateAlertType(String type);

}
