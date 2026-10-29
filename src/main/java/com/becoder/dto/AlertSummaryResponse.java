
package com.becoder.dto;

public class AlertSummaryResponse {

    private long totalAlerts;
    
    private long criticalAlerts;
    
    private long highAlerts;
    
    private long mediumAlerts;
    
    private long lowAlerts;
    
    private long expiryAlerts;
    
    private long lowStockAlerts;

    public AlertSummaryResponse() {
    }

    public AlertSummaryResponse(
            long totalAlerts,
            long criticalAlerts,
            long highAlerts,
            long mediumAlerts,
            long lowAlerts,
            long expiryAlerts,
            long lowStockAlerts) {
        this.totalAlerts = totalAlerts;
        this.criticalAlerts = criticalAlerts;
        this.highAlerts = highAlerts;
        this.mediumAlerts = mediumAlerts;
        this.lowAlerts = lowAlerts;
        this.expiryAlerts = expiryAlerts;
        this.lowStockAlerts = lowStockAlerts;
    }

    public long getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public long getCriticalAlerts() {
        return criticalAlerts;
    }

    public void setCriticalAlerts(long criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public long getHighAlerts() {
        return highAlerts;
    }

    public void setHighAlerts(long highAlerts) {
        this.highAlerts = highAlerts;
    }

    public long getMediumAlerts() {
        return mediumAlerts;
    }

    public void setMediumAlerts(long mediumAlerts) {
        this.mediumAlerts = mediumAlerts;
    }

    public long getLowAlerts() {
        return lowAlerts;
    }

    public void setLowAlerts(long lowAlerts) {
        this.lowAlerts = lowAlerts;
    }

    public long getExpiryAlerts() {
        return expiryAlerts;
    }

    public void setExpiryAlerts(long expiryAlerts) {
        this.expiryAlerts = expiryAlerts;
    }

    public long getLowStockAlerts() {
        return lowStockAlerts;
    }

    public void setLowStockAlerts(long lowStockAlerts) {
        this.lowStockAlerts = lowStockAlerts;
    }
}
