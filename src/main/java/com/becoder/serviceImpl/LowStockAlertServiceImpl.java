package com.becoder.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.InventoryOptimizationResponse;
import com.becoder.dto.LowStockAlertResponse;
import com.becoder.service.InventoryOptimizationService;
import com.becoder.service.LowStockAlertService;

@Service
@Transactional(readOnly = true)
public class LowStockAlertServiceImpl implements LowStockAlertService {

	@Autowired
	private InventoryOptimizationService inventoryOptimizationService;

	@Override
	public List<LowStockAlertResponse> getLowStockAlerts(int lowStockThreshold, int targetStock) {

		List<InventoryOptimizationResponse> lowStockMedicines = inventoryOptimizationService.getLowStockMedicines(lowStockThreshold, targetStock);

		List<LowStockAlertResponse> alerts = new ArrayList<>();

		for (InventoryOptimizationResponse medicine : lowStockMedicines) {

			LowStockAlertResponse alert = new LowStockAlertResponse();

			alert.setMedicineId(medicine.getMedicineId());
			alert.setMedicineName(medicine.getMedicineName());
			alert.setGenericName(medicine.getGenericName());
			alert.setBrand(medicine.getBrand());
			alert.setTotalAvailableStock(medicine.getTotalAvailableStock());
			alert.setLowStockThreshold(medicine.getLowStockThreshold());
			alert.setTargetStock(medicine.getTargetStock());
			alert.setSuggestedReorderQuantity(medicine.getSuggestedReorderQuantity());

			Integer stock = medicine.getTotalAvailableStock();

			if (stock == null || stock <= 0) {
				alert.setAlertSeverity("CRITICAL");
				alert.setMessage("This medicine is out of stock.");
				alert.setRecommendedAction("Review stock availability and arrange replenishment.");
			} else {
				alert.setAlertSeverity("HIGH");
				alert.setMessage("Available stock is at or below the low-stock threshold.");
				alert.setRecommendedAction("Review the suggested reorder quantity and replenish as needed.");
			}

			alerts.add(alert);
		}

		return alerts;
	}
}
