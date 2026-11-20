
package com.becoder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ManualBatchAllocationRequest {

    @NotNull(message = "Batch ID is required")
    @Min(value = 1, message = "Invalid batch ID")
    private Long batchId;

    @NotNull(message = "Batch quantity is required")
    @Min(value = 1, message = "Batch quantity must be at least 1")
    private Integer quantity;

    public ManualBatchAllocationRequest() {
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
