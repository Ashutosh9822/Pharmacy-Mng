package com.becoder.dto;

public class DashboardSummaryResponse {

    private long totalActiveMedicines;
    private long totalActiveSuppliers;
    private long totalUsableStock;
    private long totalExpiredBatches;
    private long totalExpiringSoonBatches;
    private long totalLowStockMedicines;
    private long totalReorderRecommendations;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(
            long totalActiveMedicines,
            long totalActiveSuppliers,
            long totalUsableStock,
            long totalExpiredBatches,
            long totalExpiringSoonBatches,
            long totalLowStockMedicines,
            long totalReorderRecommendations) {

        this.totalActiveMedicines = totalActiveMedicines;
        this.totalActiveSuppliers = totalActiveSuppliers;
        this.totalUsableStock = totalUsableStock;
        this.totalExpiredBatches = totalExpiredBatches;
        this.totalExpiringSoonBatches = totalExpiringSoonBatches;
        this.totalLowStockMedicines = totalLowStockMedicines;
        this.totalReorderRecommendations = totalReorderRecommendations;
    }

    public long getTotalActiveMedicines() {
        return totalActiveMedicines;
    }

    public void setTotalActiveMedicines(long totalActiveMedicines) {
        this.totalActiveMedicines = totalActiveMedicines;
    }

    public long getTotalActiveSuppliers() {
        return totalActiveSuppliers;
    }

    public void setTotalActiveSuppliers(long totalActiveSuppliers) {
        this.totalActiveSuppliers = totalActiveSuppliers;
    }

    public long getTotalUsableStock() {
        return totalUsableStock;
    }

    public void setTotalUsableStock(long totalUsableStock) {
        this.totalUsableStock = totalUsableStock;
    }

    public long getTotalExpiredBatches() {
        return totalExpiredBatches;
    }

    public void setTotalExpiredBatches(long totalExpiredBatches) {
        this.totalExpiredBatches = totalExpiredBatches;
    }

    public long getTotalExpiringSoonBatches() {
        return totalExpiringSoonBatches;
    }

    public void setTotalExpiringSoonBatches(long totalExpiringSoonBatches) {
        this.totalExpiringSoonBatches = totalExpiringSoonBatches;
    }

    public long getTotalLowStockMedicines() {
        return totalLowStockMedicines;
    }

    public void setTotalLowStockMedicines(long totalLowStockMedicines) {
        this.totalLowStockMedicines = totalLowStockMedicines;
    }

    public long getTotalReorderRecommendations() {
        return totalReorderRecommendations;
    }

    public void setTotalReorderRecommendations(long totalReorderRecommendations) {
        this.totalReorderRecommendations = totalReorderRecommendations;
    }
}
