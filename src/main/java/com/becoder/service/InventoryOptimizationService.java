package com.becoder.service;

import java.util.List;

import com.becoder.dto.InventoryOptimizationResponse;

public interface InventoryOptimizationService {

	List<InventoryOptimizationResponse> getInventoryReport(int lowStockThreshold, int targetStock);

	List<InventoryOptimizationResponse> getLowStockMedicines(int lowStockThreshold, int targetStock);

}
