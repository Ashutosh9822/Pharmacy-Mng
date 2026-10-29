
package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.AlertSummaryResponse;
import com.becoder.dto.UnifiedAlertResponse;
import com.becoder.service.AlertSummaryService;
import com.becoder.service.UnifiedAlertService;

@Service
@Transactional(readOnly = true)
public class AlertSummaryServiceImpl implements AlertSummaryService {

	@Autowired
	private UnifiedAlertService unifiedAlertService;

	@Override
	public AlertSummaryResponse getAlertSummary(int expiryDays, int lowStockThreshold, int targetStock) {

		List<UnifiedAlertResponse> alerts = unifiedAlertService.getAllAlerts(expiryDays, lowStockThreshold, targetStock,null, null);

		long criticalAlerts = 0;
		long highAlerts = 0;
		long mediumAlerts = 0;
		long lowAlerts = 0;
		long expiryAlerts = 0;
		long lowStockAlerts = 0;

		for (UnifiedAlertResponse alert : alerts) {

			String severity = alert.getAlertSeverity();
			String type = alert.getAlertType();

			if ("CRITICAL".equalsIgnoreCase(severity)) {
				criticalAlerts++;
			} else if ("HIGH".equalsIgnoreCase(severity)) {
				highAlerts++;
			} else if ("MEDIUM".equalsIgnoreCase(severity)) {
				mediumAlerts++;
			} else if ("LOW".equalsIgnoreCase(severity)) {
				lowAlerts++;
			}

			if ("EXPIRY".equalsIgnoreCase(type)) {
				expiryAlerts++;
			} else if ("LOW_STOCK".equalsIgnoreCase(type)) {
				lowStockAlerts++;
			}
		}

		return new AlertSummaryResponse(alerts.size(), criticalAlerts, highAlerts, mediumAlerts, lowAlerts, expiryAlerts, lowStockAlerts);
	}
}
