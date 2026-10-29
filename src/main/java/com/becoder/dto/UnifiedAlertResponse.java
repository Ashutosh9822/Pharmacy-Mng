
package com.becoder.dto;

import java.time.LocalDate;

public class UnifiedAlertResponse {

	private String alertType;
	private String alertSeverity;
	private String message;
	private String recommendedAction;

	private Long medicineId;
	private String medicineName;

	private Long batchId;
	private String batchNumber;
	private LocalDate expiryDate;
	private Long daysUntilExpiry;

	private Integer totalAvailableStock;
	private Integer suggestedReorderQuantity;

	public UnifiedAlertResponse() {
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getAlertSeverity() {
		return alertSeverity;
	}

	public void setAlertSeverity(String alertSeverity) {
		this.alertSeverity = alertSeverity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRecommendedAction() {
		return recommendedAction;
	}

	public void setRecommendedAction(String recommendedAction) {
		this.recommendedAction = recommendedAction;
	}

	public Long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Long medicineId) {
		this.medicineId = medicineId;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Long getDaysUntilExpiry() {
		return daysUntilExpiry;
	}

	public void setDaysUntilExpiry(Long daysUntilExpiry) {
		this.daysUntilExpiry = daysUntilExpiry;
	}

	public Integer getTotalAvailableStock() {
		return totalAvailableStock;
	}

	public void setTotalAvailableStock(Integer totalAvailableStock) {
		this.totalAvailableStock = totalAvailableStock;
	}

	public Integer getSuggestedReorderQuantity() {
		return suggestedReorderQuantity;
	}

	public void setSuggestedReorderQuantity(Integer suggestedReorderQuantity) {
		this.suggestedReorderQuantity = suggestedReorderQuantity;
	}
}
