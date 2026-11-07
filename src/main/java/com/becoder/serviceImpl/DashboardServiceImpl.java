package com.becoder.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.DashboardSummaryResponse;
import com.becoder.dto.ExpiryBatchResponse;
import com.becoder.dto.InventoryOptimizationResponse;
import com.becoder.dto.SupplierReorderResponse;
import com.becoder.entity.Medicine;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.repository.MedicineRepository;
import com.becoder.repository.SupplierRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.DashboardService;
import com.becoder.service.ExpiryTrackingService;
import com.becoder.service.InventoryOptimizationService;
import com.becoder.service.SupplierReorderService;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;
    private static final int DEFAULT_TARGET_STOCK = 50;
    private static final int EXPIRING_SOON_DAYS = 30;

    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    private final ExpiryTrackingService expiryTrackingService;
    private final InventoryOptimizationService inventoryOptimizationService;
    private final SupplierReorderService supplierReorderService;
    private final CurrentUserService currentUserService;

    public DashboardServiceImpl(
            MedicineRepository medicineRepository,
            SupplierRepository supplierRepository,
            MedicineBatchRepository medicineBatchRepository,
            ExpiryTrackingService expiryTrackingService,
            InventoryOptimizationService inventoryOptimizationService,
            SupplierReorderService supplierReorderService,
            CurrentUserService currentUserService) {

        this.medicineRepository = medicineRepository;
        this.supplierRepository = supplierRepository;
        this.medicineBatchRepository = medicineBatchRepository;
        this.expiryTrackingService = expiryTrackingService;
        this.inventoryOptimizationService = inventoryOptimizationService;
        this.supplierReorderService = supplierReorderService;
        this.currentUserService = currentUserService;
    }

    @Override
    public DashboardSummaryResponse getDashboardSummary() {

        // Get authenticated user's pharmacy ID.
        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        // 1. Count active medicines belonging to this pharmacy.
        List<Medicine> activeMedicines = medicineRepository.findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(pharmacyId);

        long totalActiveMedicines = activeMedicines.size();

        // 2. Count active suppliers belonging to this pharmacy.
        long totalActiveSuppliers = supplierRepository.findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(pharmacyId).size();

        // 3. Calculate total usable stock from this pharmacy's non-expired batches.
        List<Object[]> stockRows = medicineBatchRepository.findUsableStockByMedicine(LocalDate.now(), pharmacyId);

        long totalUsableStock = 0L;

        for (Object[] row : stockRows) {
            if (row[1] != null) {
                totalUsableStock += ((Number) row[1]).longValue();
            }
        }

        // 4. Count expired batches belonging to this pharmacy.
        List<ExpiryBatchResponse> expiredBatches = expiryTrackingService.getExpiredBatches();

        long totalExpiredBatches = expiredBatches.size();

        // 5. Count batches expiring within the next 30 days.
        List<ExpiryBatchResponse> expiringSoonBatches = expiryTrackingService.getExpiringSoonBatches(EXPIRING_SOON_DAYS);

        long totalExpiringSoonBatches = expiringSoonBatches.size();

        // 6. Count medicines requiring replenishment.
        List<InventoryOptimizationResponse> lowStockMedicines =
                inventoryOptimizationService.getLowStockMedicines(
                        DEFAULT_LOW_STOCK_THRESHOLD,
                        DEFAULT_TARGET_STOCK);

        long totalLowStockMedicines = lowStockMedicines.size();

        // 7. Count supplier reorder recommendations.
        List<SupplierReorderResponse> reorderRecommendations =
                supplierReorderService.getReorderRecommendations(
                        DEFAULT_LOW_STOCK_THRESHOLD,
                        DEFAULT_TARGET_STOCK);

        long totalReorderRecommendations =
                reorderRecommendations.size();

        return new DashboardSummaryResponse(
                totalActiveMedicines,
                totalActiveSuppliers,
                totalUsableStock,
                totalExpiredBatches,
                totalExpiringSoonBatches,
                totalLowStockMedicines,
                totalReorderRecommendations);
    }
}
