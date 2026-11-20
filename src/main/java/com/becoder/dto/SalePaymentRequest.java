
package com.becoder.dto;

import java.math.BigDecimal;

import com.becoder.entity.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SalePaymentRequest {

	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;

	@NotNull(message = "Payment amount is required")
	@DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
	private BigDecimal amount;

	@Size(max = 100, message = "Reference number cannot exceed 100 characters")
	private String referenceNumber;

	public SalePaymentRequest() {
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}
