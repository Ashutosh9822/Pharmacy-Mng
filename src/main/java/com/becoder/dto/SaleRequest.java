
package com.becoder.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class SaleRequest {

	@Positive(message = "Invalid customer ID")
	private Long customerId;

	@NotEmpty(message = "At least one medicine item is required")
	@Size(max = 100, message = "A bill cannot contain more than 100 items")
	@Valid
	private List<SaleItemRequest> items;

	@NotEmpty(message = "At least one payment is required")
	@Size(max = 10, message = "Too many payment entries")
	@Valid
	private List<SalePaymentRequest> payments;

	@NotNull(message = "Discount amount is required")
	@DecimalMin(value = "0.00", message = "Discount cannot be negative")
	private BigDecimal discountAmount = BigDecimal.ZERO;

	public SaleRequest() {
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<SaleItemRequest> getItems() {
		return items;
	}

	public void setItems(List<SaleItemRequest> items) {
		this.items = items;
	}

	public List<SalePaymentRequest> getPayments() {
		return payments;
	}

	public void setPayments(List<SalePaymentRequest> payments) {
		this.payments = payments;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
}
