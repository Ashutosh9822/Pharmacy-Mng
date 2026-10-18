
package com.becoder.dto;

import java.time.LocalDateTime;

public class CategoryResponse {

    private Long id;
    
    private String name;
    
    private String description;
    
    private boolean isActive;
    
    private boolean isDeleted;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedOn;
    
    private Long updatedBy;

    public CategoryResponse() {
    }

    public CategoryResponse(
            Long id,
            String name,
            String description,
            boolean isActive,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedOn,
            Long updatedBy) {

        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
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
