package com.becoder.service;

import com.becoder.dto.AlertSummaryResponse;

public interface AlertSummaryService {

	AlertSummaryResponse getAlertSummary(int expiryDays, int lowStockThreshold, int targetStock);

}
