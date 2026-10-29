package com.becoder.service;

import java.util.List;

import com.becoder.dto.LowStockAlertResponse;

public interface LowStockAlertService {

	List<LowStockAlertResponse> getLowStockAlerts(int lowStockThreshold, int targetStock);

}
