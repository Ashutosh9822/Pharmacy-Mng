
package com.becoder.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.ExpiryAlertResponse;
import com.becoder.dto.ExpiryBatchResponse;
import com.becoder.service.ExpiryAlertService;
import com.becoder.service.ExpiryTrackingService;

@Service
@Transactional(readOnly = true)
public class ExpiryAlertServiceImpl implements ExpiryAlertService {

	@Autowired
    private ExpiryTrackingService expiryTrackingService;

    @Override
    public List<ExpiryAlertResponse> getExpiryAlerts(int days) {

        if (days < 1) {
            throw new IllegalArgumentException(
                    "Days must be greater than zero.");
        }

        List<ExpiryBatchResponse> batches = expiryTrackingService.getExpiryReport();

        List<ExpiryAlertResponse> alerts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        for (ExpiryBatchResponse batch : batches) {

            LocalDate expiryDate = batch.getExpiryDate();

            if (expiryDate == null || expiryDate.isAfter(endDate)) {
                continue;
            }

            long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, expiryDate);
            
            ExpiryAlertResponse alert = new ExpiryAlertResponse();
            alert.setBatchId(batch.getBatchId());
            alert.setMedicineId(batch.getMedicineId());
            alert.setMedicineName(batch.getMedicineName());
            alert.setBatchNumber(batch.getBatchNumber());
            alert.setExpiryDate(expiryDate);
            alert.setQuantity(batch.getQuantity());
            alert.setDaysUntilExpiry(daysUntilExpiry);

            if (daysUntilExpiry < 0) {
                alert.setAlertSeverity("CRITICAL");
                alert.setMessage("This medicine batch has expired.");
                alert.setRecommendedAction(
                        "Immediately segregate the expired stock and follow "
                        + "your pharmacy's approved disposal procedure.");
            } else if (daysUntilExpiry <= 7) {
                alert.setAlertSeverity("HIGH");
                alert.setMessage(
                        "This medicine batch expires within 7 days.");
                alert.setRecommendedAction(
                        "Review the batch immediately and prioritize "
                        + "appropriate action under pharmacy policy.");
            } else if (daysUntilExpiry <= 15) {
                alert.setAlertSeverity("MEDIUM");
                alert.setMessage(
                        "This medicine batch expires within 15 days.");
                alert.setRecommendedAction(
                        "Review stock and plan appropriate action.");
            } else {
                alert.setAlertSeverity("LOW");
                alert.setMessage(
                        "This medicine batch is approaching its expiry date.");
                alert.setRecommendedAction(
                        "Monitor this batch and review stock rotation.");
            }

            alerts.add(alert);
        }

        return alerts;
    }
}
