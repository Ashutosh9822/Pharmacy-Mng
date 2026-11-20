package com.becoder.dto;

import java.time.LocalDate;

public class BatchAllocationResponse {

    private Long batchId;
    
    private String batchNumber;
    
    private Integer quantity;
    
    private LocalDate expiryDate;

    public BatchAllocationResponse() {
    }

    public BatchAllocationResponse(
            Long batchId,
            String batchNumber,
            Integer quantity,
            LocalDate expiryDate) {

        this.batchId = batchId;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
