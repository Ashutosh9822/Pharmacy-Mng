package com.becoder.dto;

public class LowStockAlertResponse {

    private Long medicineId;
    
    private String medicineName;
    
    private String genericName;
    
    private String brand;
    
    private Integer totalAvailableStock;
    
    private Integer lowStockThreshold;
    
    private Integer targetStock;
    
    private Integer suggestedReorderQuantity;
    
    private String alertSeverity;
    
    private String message;
    
    private String recommendedAction;

    public LowStockAlertResponse() {
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

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getTotalAvailableStock() {
        return totalAvailableStock;
    }

    public void setTotalAvailableStock(Integer totalAvailableStock) {
        this.totalAvailableStock = totalAvailableStock;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Integer getTargetStock() {
        return targetStock;
    }

    public void setTargetStock(Integer targetStock) {
        this.targetStock = targetStock;
    }

    public Integer getSuggestedReorderQuantity() {
        return suggestedReorderQuantity;
    }

    public void setSuggestedReorderQuantity(Integer suggestedReorderQuantity) {
        this.suggestedReorderQuantity = suggestedReorderQuantity;
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
}
