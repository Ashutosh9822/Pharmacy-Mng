package com.becoder.dto;

import java.math.BigDecimal;

public class SaleMedicineSearchResponse {

    private Long medicineId;
    
    private String name;
    
    private String genericName;
    
    private String brand;
    
    private String dosageForm;
    
    private String strength;
    
    private String barcode;
    
    private String hsnCode;
    
    private BigDecimal gstRate;

    public SaleMedicineSearchResponse() {
    }

    public SaleMedicineSearchResponse(
            Long medicineId,
            String name,
            String genericName,
            String brand,
            String dosageForm,
            String strength,
            String barcode,
            String hsnCode,
            BigDecimal gstRate) {

        this.medicineId = medicineId;
        this.name = name;
        this.genericName = genericName;
        this.brand = brand;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.barcode = barcode;
        this.hsnCode = hsnCode;
        this.gstRate = gstRate;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
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

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public BigDecimal getGstRate() {
        return gstRate;
    }

    public void setGstRate(BigDecimal gstRate) {
        this.gstRate = gstRate;
    }
}