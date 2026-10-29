package com.becoder.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.ExpiryAlertResponse;
import com.becoder.dto.LowStockAlertResponse;
import com.becoder.dto.UnifiedAlertResponse;
import com.becoder.service.ExpiryAlertService;
import com.becoder.service.LowStockAlertService;
import com.becoder.service.UnifiedAlertService;

@Service
@Transactional(readOnly = true)
public class UnifiedAlertServiceImpl implements UnifiedAlertService {

	@Autowired
	private ExpiryAlertService expiryAlertService;
	
	@Autowired
	private LowStockAlertService lowStockAlertService;

	@Override
	public List<UnifiedAlertResponse> getAllAlerts(int expiryDays, int lowStockThreshold, int targetStock,String severity,String type) {

		List<UnifiedAlertResponse> allAlerts = new ArrayList<>();

		// 1. Convert expiry alerts into unified alerts.
		List<ExpiryAlertResponse> expiryAlerts = expiryAlertService.getExpiryAlerts(expiryDays);

		for (ExpiryAlertResponse expiry : expiryAlerts) {

			UnifiedAlertResponse alert = new UnifiedAlertResponse();

			alert.setAlertType("EXPIRY");
			alert.setAlertSeverity(expiry.getAlertSeverity());
			alert.setMessage(expiry.getMessage());
			alert.setRecommendedAction(expiry.getRecommendedAction());

			alert.setMedicineId(expiry.getMedicineId());
			alert.setMedicineName(expiry.getMedicineName());

			alert.setBatchId(expiry.getBatchId());
			alert.setBatchNumber(expiry.getBatchNumber());
			alert.setExpiryDate(expiry.getExpiryDate());
			alert.setDaysUntilExpiry(expiry.getDaysUntilExpiry());

			allAlerts.add(alert);
		}

		// 2. Convert low-stock alerts into unified alerts.
		List<LowStockAlertResponse> lowStockAlerts = lowStockAlertService.getLowStockAlerts(lowStockThreshold,targetStock);
		for (LowStockAlertResponse lowStock : lowStockAlerts) {
			
			UnifiedAlertResponse alert = new UnifiedAlertResponse();

			alert.setAlertType("LOW_STOCK");
			alert.setAlertSeverity(lowStock.getAlertSeverity());
			alert.setMessage(lowStock.getMessage());
			alert.setRecommendedAction(lowStock.getRecommendedAction());

			alert.setMedicineId(lowStock.getMedicineId());
			alert.setMedicineName(lowStock.getMedicineName());

			alert.setTotalAvailableStock(lowStock.getTotalAvailableStock());
			alert.setSuggestedReorderQuantity(lowStock.getSuggestedReorderQuantity());

			allAlerts.add(alert);
		}
		
		allAlerts.sort(
			    Comparator.comparingInt(alert -> {
//			        String severity = alert.getAlertSeverity();

			        if ("CRITICAL".equalsIgnoreCase(severity)) {
			            return 1;
			        } else if ("HIGH".equalsIgnoreCase(severity)) {
			            return 2;
			        } else if ("MEDIUM".equalsIgnoreCase(severity)) {
			            return 3;
			        } else if ("LOW".equalsIgnoreCase(severity)) {
			            return 4;
			        }

			        return 5;
			    })
			);
		
		// Filter alerts by severity and type
		if (severity != null && !severity.isBlank()) {
		    allAlerts.removeIf(alert ->
		        alert.getAlertSeverity() == null
		        || !alert.getAlertSeverity()
		                  .equalsIgnoreCase(severity.trim())
		    );
		}

		if (type != null && !type.isBlank()) {
		    allAlerts.removeIf(alert ->
		        alert.getAlertType() == null
		        || !alert.getAlertType()
		                  .equalsIgnoreCase(type.trim())
		    );
		}

		return allAlerts;
	}
}
