
package com.becoder.dto;

import java.math.BigDecimal;
import java.util.List;

public class SaleItemResponse {

	private Long medicineId;
	
	private String medicineName;

	private Integer quantity;

	private BigDecimal unitPrice;
	
	private BigDecimal discountAmount;
	
	private BigDecimal gstRate;
	
	private BigDecimal taxAmount;
	
	private BigDecimal lineTotal;
	
	private String hsnCode;

	private List<BatchAllocationResponse> batchAllocations;

	public SaleItemResponse() {
	}

	public SaleItemResponse(Long medicineId, String medicineName, Integer quantity, BigDecimal unitPrice,
			BigDecimal discountAmount, BigDecimal gstRate, BigDecimal taxAmount, BigDecimal lineTotal,
			List<BatchAllocationResponse> batchAllocations) {

		this.medicineId = medicineId;
		this.medicineName = medicineName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.discountAmount = discountAmount;
		this.gstRate = gstRate;
		this.taxAmount = taxAmount;
		this.lineTotal = lineTotal;
		this.batchAllocations = batchAllocations;
	}
	
	public String getHsnCode() {
	    return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
	    this.hsnCode = hsnCode;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getGstRate() {
		return gstRate;
	}

	public void setGstRate(BigDecimal gstRate) {
		this.gstRate = gstRate;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(BigDecimal lineTotal) {
		this.lineTotal = lineTotal;
	}

	public List<BatchAllocationResponse> getBatchAllocations() {
		return batchAllocations;
	}

	public void setBatchAllocations(List<BatchAllocationResponse> batchAllocations) {
		this.batchAllocations = batchAllocations;
	}
}
