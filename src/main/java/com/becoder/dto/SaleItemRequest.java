
package com.becoder.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SaleItemRequest {

    @NotNull(message = "Medicine ID is required")
    @Min(value = 1, message = "Invalid medicine ID")
    private Long medicineId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Batch selection mode is required")
    private BatchSelectionMode batchSelectionMode;

    @Valid
    @Size(max = 50, message = "Too many batch allocations")
    private List<ManualBatchAllocationRequest> manualAllocations;

    public SaleItemRequest() {
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BatchSelectionMode getBatchSelectionMode() {
        return batchSelectionMode;
    }

    public void setBatchSelectionMode(BatchSelectionMode batchSelectionMode) {
        this.batchSelectionMode = batchSelectionMode;
    }

    public List<ManualBatchAllocationRequest> getManualAllocations() {
        return manualAllocations;
    }

    public void setManualAllocations(
            List<ManualBatchAllocationRequest> manualAllocations) {
        this.manualAllocations = manualAllocations;
    }
}
