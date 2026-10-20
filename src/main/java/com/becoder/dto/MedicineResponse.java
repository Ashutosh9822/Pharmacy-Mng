
package com.becoder.dto;

import java.time.LocalDateTime;

public class MedicineResponse {

    private Long id;
    
    private String name;
    
    private String genericName;
    
    private String brand;
    
    private String dosageForm;
    
    private String strength;
    
    private String barcode;
    
    private String description;

    private Long categoryId;
    
    private String categoryName;

    private boolean isActive;
    
    private boolean isDeleted;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedOn;
    
    private Long updatedBy;

    public MedicineResponse() {
    }

    public MedicineResponse(
            Long id,
            String name,
            String genericName,
            String brand,
            String dosageForm,
            String strength,
            String barcode,
            String description,
            Long categoryId,
            String categoryName,
            boolean isActive,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedOn,
            Long updatedBy) {

        this.id = id;
        this.name = name;
        this.genericName = genericName;
        this.brand = brand;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.barcode = barcode;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}
