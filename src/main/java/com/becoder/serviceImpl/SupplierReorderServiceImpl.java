package com.becoder.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.SupplierReorderResponse;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.service.SupplierReorderService;

@Service
@Transactional(readOnly = true)
public class SupplierReorderServiceImpl implements SupplierReorderService {

	@Autowired
    private MedicineBatchRepository batchRepository;

    @Override
    public List<SupplierReorderResponse> getReorderRecommendations(int lowStockThreshold, int targetStock) {

        validateParameters(lowStockThreshold, targetStock);

        LocalDate today = LocalDate.now();

        List<Object[]> rows =
                batchRepository.findUsableStockBySupplierAndMedicine(today);

        List<SupplierReorderResponse> responses = new ArrayList<>();

        for (Object[] row : rows) {
            int stock = ((Number) row[6]).intValue();

            // Only recommend reordering medicines at or below threshold.
            if (stock > lowStockThreshold) {
                continue;
            }

            responses.add(buildResponse(
                    row, stock, lowStockThreshold, targetStock));
        }

        return responses;
    }

    @Override
    public List<SupplierReorderResponse> getReorderRecommendationsBySupplier(Long supplierId,int lowStockThreshold,int targetStock) {
        if (supplierId == null || supplierId < 1) {
            throw new IllegalArgumentException(
                    "Supplier ID must be a positive number");
        }
        
        return getReorderRecommendations(lowStockThreshold, targetStock)
                .stream()
                .filter(item -> supplierId.equals(item.getSupplierId()))
                .toList();
    }

    private SupplierReorderResponse buildResponse(Object[] row,int stock,int lowStockThreshold,int targetStock) {
        BigDecimal totalPurchaseValue = row[7] == null ? BigDecimal.ZERO : new BigDecimal(row[7].toString());

        BigDecimal averagePrice = stock > 0 ? totalPurchaseValue.divide(
                        BigDecimal.valueOf(stock),
                        2,
                        RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        int reorderQuantity = Math.max(0, targetStock - stock);

        BigDecimal reorderCost = averagePrice.multiply(
                BigDecimal.valueOf(reorderQuantity))
                .setScale(2, RoundingMode.HALF_UP);

        SupplierReorderResponse response = new SupplierReorderResponse();
        response.setSupplierId(((Number) row[0]).longValue());
        response.setSupplierName((String) row[1]);
        response.setMedicineId(((Number) row[2]).longValue());
        response.setMedicineName((String) row[3]);
        response.setGenericName((String) row[4]);
        response.setBrand((String) row[5]);
        response.setTotalAvailableStock(stock);
        response.setLowStockThreshold(lowStockThreshold);
        response.setTargetStock(targetStock);
        response.setSuggestedReorderQuantity(reorderQuantity);
        response.setWeightedAveragePurchasePrice(averagePrice);
        response.setEstimatedReorderCost(reorderCost);

        if (stock == 0) {
            response.setReorderPriority("CRITICAL");
        } else {
            response.setReorderPriority("HIGH");
        }

        return response;
    }

    private void validateParameters(int lowStockThreshold, int targetStock) {
        if (lowStockThreshold < 0) {
            throw new IllegalArgumentException("Low-stock threshold cannot be negative");
        }

        if (targetStock < 1) {
            throw new IllegalArgumentException("Target stock must be at least 1");
        }

        if (targetStock <= lowStockThreshold) {
            throw new IllegalArgumentException("Target stock must exceed the low-stock threshold");
        }
    }
}
