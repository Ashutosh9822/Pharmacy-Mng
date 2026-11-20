package com.becoder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PharmacyProfileRequest {

    @NotBlank(message = "Pharmacy name is required")
    @Size(max = 150, message = "Pharmacy name cannot exceed 150 characters")
    private String pharmacyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @Pattern(
        regexp = "^(\\+?[0-9\\s()-]{7,20})?$",
        message = "Please provide a valid phone number"
    )
    private String phone;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    public PharmacyProfileRequest() {
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
