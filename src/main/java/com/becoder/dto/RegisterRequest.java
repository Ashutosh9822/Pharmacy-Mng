
package com.becoder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    // Pharmacy details
    @NotBlank(message = "Pharmacy name is required")
    @Size(max = 150, message = "Pharmacy name cannot exceed 150 characters")
    private String pharmacyName;

    @NotBlank(message = "Pharmacy email is required")
    @Email(message = "Please provide a valid pharmacy email")
    @Size(max = 150, message = "Pharmacy email cannot exceed 150 characters")
    private String pharmacyEmail;

    @Size(max = 20, message = "Pharmacy phone cannot exceed 20 characters")
    private String pharmacyPhone;

    @Size(max = 255, message = "Pharmacy address cannot exceed 255 characters")
    private String pharmacyAddress;

    // Pharmacy owner details
    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name cannot exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Owner email is required")
    @Email(message = "Please provide a valid owner email")
    @Size(max = 150, message = "Owner email cannot exceed 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72,
          message = "Password must contain between 8 and 72 characters")
    private String password;

    public RegisterRequest() {
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getPharmacyEmail() {
        return pharmacyEmail;
    }

    public void setPharmacyEmail(String pharmacyEmail) {
        this.pharmacyEmail = pharmacyEmail;
    }

    public String getPharmacyPhone() {
        return pharmacyPhone;
    }

    public void setPharmacyPhone(String pharmacyPhone) {
        this.pharmacyPhone = pharmacyPhone;
    }

    public String getPharmacyAddress() {
        return pharmacyAddress;
    }

    public void setPharmacyAddress(String pharmacyAddress) {
        this.pharmacyAddress = pharmacyAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
