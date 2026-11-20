
package com.becoder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.becoder.entity.PaymentMethod;

public class SalePaymentResponse {

	private Long id;
	private PaymentMethod paymentMethod;
	private BigDecimal amount;
	private String referenceNumber;
	private LocalDateTime paymentDate;

	public SalePaymentResponse() {
	}

	public SalePaymentResponse(Long id, PaymentMethod paymentMethod, BigDecimal amount, String referenceNumber,
			LocalDateTime paymentDate) {

		this.id = id;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.referenceNumber = referenceNumber;
		this.paymentDate = paymentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
}
