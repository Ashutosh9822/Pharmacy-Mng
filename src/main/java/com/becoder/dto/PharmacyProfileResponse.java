package com.becoder.dto;

import java.time.LocalDateTime;

public class PharmacyProfileResponse {

    private Long id;
    
    private String pharmacyName;
    
    private String email;
    
    private String phone;
    
    private String address;
    
    private boolean active;
    
    private LocalDateTime createdOn;
    
    private LocalDateTime updatedOn;

    public PharmacyProfileResponse() {
    }

    public PharmacyProfileResponse(
            Long id,
            String pharmacyName,
            String email,
            String phone,
            String address,
            boolean active,
            LocalDateTime createdOn,
            LocalDateTime updatedOn) {

        this.id = id;
        this.pharmacyName = pharmacyName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.active = active;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public Long getId() {
        return id;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
}
