
package com.becoder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import jakarta.validation.constraints.Size;

public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 150, message = "Medicine name cannot exceed 150 characters")
    private String name;

    @Size(max = 150, message = "Generic name cannot exceed 150 characters")
    private String genericName;

    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;

    @Size(max = 50, message = "Dosage form cannot exceed 50 characters")
    private String dosageForm;

    @Size(max = 50, message = "Strength cannot exceed 50 characters")
    private String strength;

    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    private String barcode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be greater than zero")
    private Long categoryId;

    private Boolean isActive;

    public MedicineRequest() {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
