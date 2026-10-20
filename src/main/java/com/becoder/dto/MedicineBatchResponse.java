package com.becoder.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MedicineBatchResponse {

    private Long id;

    private Long medicineId;
    
    private String medicineName;

    private Long supplierId;
    
    private String supplierName;

    private String batchNumber;
    
    private Integer quantity;

    private LocalDate manufacturingDate;
    
    private LocalDate expiryDate;

    private BigDecimal purchasePrice;
    
    private BigDecimal sellingPrice;

    private boolean isDeleted;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedOn;
    
    private Long updatedBy;

    public MedicineBatchResponse() {
    }

    public MedicineBatchResponse(
            Long id,
            Long medicineId,
            String medicineName,
            Long supplierId,
            String supplierName,
            String batchNumber,
            Integer quantity,
            LocalDate manufacturingDate,
            LocalDate expiryDate,
            BigDecimal purchasePrice,
            BigDecimal sellingPrice,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedOn,
            Long updatedBy) {

        this.id = id;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}
