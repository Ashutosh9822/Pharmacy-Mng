package com.becoder.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleBatchResponse {

    private Long batchId;
    
    private String batchNumber;
    
    private LocalDate expiryDate;
    
    private Integer availableQuantity;
    
    private BigDecimal sellingPrice;

    public SaleBatchResponse() {
    }

    public SaleBatchResponse(
            Long batchId,
            String batchNumber,
            LocalDate expiryDate,
            Integer availableQuantity,
            BigDecimal sellingPrice) {

        this.batchId = batchId;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.availableQuantity = availableQuantity;
        this.sellingPrice = sellingPrice;
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

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}