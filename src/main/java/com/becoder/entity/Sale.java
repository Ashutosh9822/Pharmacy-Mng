
package com.becoder.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "sales",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_sales_pharmacy_invoice",
            columnNames = {"pharmacy_id", "invoice_number"}
        )
    },
    indexes = {
        @Index(
            name = "idx_sales_pharmacy",
            columnList = "pharmacy_id"
        ),
        @Index(
            name = "idx_sales_customer",
            columnList = "customer_id"
        ),
        @Index(
            name = "idx_sales_created",
            columnList = "created_on"
        )
    }
)
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(
        name = "invoice_number",
        nullable = false,
        length = 50
    )
    private String invoiceNumber;

    @Column(
        name = "subtotal",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal subtotal;

    @Column(
        name = "discount_amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(
        name = "tax_amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(
        name = "total_amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "sale_status",
        nullable = false,
        length = 20
    )
    private SaleStatus saleStatus = SaleStatus.COMPLETED;

    @Column(
        name = "created_by",
        nullable = false
    )
    private Long createdBy;

    @Column(
        name = "created_on",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdOn;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }

    public Sale() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SaleStatus getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(SaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
