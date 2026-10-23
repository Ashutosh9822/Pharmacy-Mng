package com.becoder.dto;

import java.math.BigDecimal;

public class SupplierReorderResponse {

    private Long supplierId;
    private String supplierName;

    private Long medicineId;
    private String medicineName;
    private String genericName;
    private String brand;

    private Integer totalAvailableStock;
    private Integer lowStockThreshold;
    private Integer targetStock;
    private Integer suggestedReorderQuantity;

    private BigDecimal weightedAveragePurchasePrice;
    private BigDecimal estimatedReorderCost;

    private String reorderPriority;

    public SupplierReorderResponse() {
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public void setSuggestedReorderQuantity(
            Integer suggestedReorderQuantity) {
        this.suggestedReorderQuantity = suggestedReorderQuantity;
    }

    public BigDecimal getWeightedAveragePurchasePrice() {
        return weightedAveragePurchasePrice;
    }

    public void setWeightedAveragePurchasePrice(
            BigDecimal weightedAveragePurchasePrice) {
        this.weightedAveragePurchasePrice = weightedAveragePurchasePrice;
    }

    public BigDecimal getEstimatedReorderCost() {
        return estimatedReorderCost;
    }

    public void setEstimatedReorderCost(
            BigDecimal estimatedReorderCost) {
        this.estimatedReorderCost = estimatedReorderCost;
    }

    public String getReorderPriority() {
        return reorderPriority;
    }

    public void setReorderPriority(String reorderPriority) {
        this.reorderPriority = reorderPriority;
    }
}
