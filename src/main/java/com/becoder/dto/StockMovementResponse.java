
package com.becoder.dto;

import java.time.LocalDateTime;

import com.becoder.entity.StockMovementType;

public class StockMovementResponse {

    private Long id;
    
    private Long batchId;
    
    private String batchNumber;
    
    private Long medicineId;
    
    private String medicineName;
    
    private StockMovementType movementType;
    
    private Integer quantity;
    
    private Integer quantityAfterMovement;
    
    private String reason;
    
    private String referenceNumber;
    
    private LocalDateTime createdAt;

    public StockMovementResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StockMovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(StockMovementType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityAfterMovement() {
        return quantityAfterMovement;
    }

    public void setQuantityAfterMovement(Integer quantityAfterMovement) {
        this.quantityAfterMovement = quantityAfterMovement;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
