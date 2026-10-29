package com.becoder.service;

import java.util.List;

import com.becoder.dto.UnifiedAlertResponse;

public interface UnifiedAlertService {

	List<UnifiedAlertResponse> getAllAlerts(int expiryDays, int lowStockThreshold, int targetStock, String severity,String type);
	
}
