package com.becoder.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.InventoryOptimizationResponse;
import com.becoder.entity.Medicine;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.repository.MedicineRepository;
import com.becoder.service.InventoryOptimizationService;

@Service
@Transactional(readOnly = true)
public class InventoryOptimizationServiceImpl implements InventoryOptimizationService {

	@Autowired
	private MedicineBatchRepository batchRepository;
	
	@Autowired
	private MedicineRepository medicineRepository;

	@Override
	public List<InventoryOptimizationResponse> getInventoryReport(int lowStockThreshold, int targetStock) {

		validateParameters(lowStockThreshold, targetStock);

		LocalDate today = LocalDate.now();

		List<Object[]> stockRows = batchRepository.findUsableStockByMedicine(today);

		Map<Long, Integer> stockByMedicine = new HashMap<>();

		for (Object[] row : stockRows) {
			Long medicineId = ((Number) row[0]).longValue();
			Integer quantity = ((Number) row[1]).intValue();

			stockByMedicine.put(medicineId, quantity);
		}

		List<InventoryOptimizationResponse> responses = new ArrayList<>();

		List<Medicine> medicines = medicineRepository.findByIsActiveTrueAndIsDeletedFalse();

		for (Medicine medicine : medicines) {
			int stock = stockByMedicine.getOrDefault(medicine.getId(), 0);
			InventoryOptimizationResponse response = buildResponse(medicine, stock, lowStockThreshold, targetStock);
			responses.add(response);
		}

		return responses;
	}

	@Override
	public List<InventoryOptimizationResponse> getLowStockMedicines(int lowStockThreshold, int targetStock) {
		return getInventoryReport(lowStockThreshold, targetStock).stream().filter(response -> "LOW_STOCK".equals(response.getStockStatus())).toList();
	}

	private InventoryOptimizationResponse buildResponse(Medicine medicine, int stock, int lowStockThreshold,int targetStock) {
		InventoryOptimizationResponse response = new InventoryOptimizationResponse();
		response.setMedicineId(medicine.getId());
		response.setMedicineName(medicine.getName());
		response.setGenericName(medicine.getGenericName());
		response.setBrand(medicine.getBrand());
		response.setTotalAvailableStock(stock);
		response.setLowStockThreshold(lowStockThreshold);
		response.setTargetStock(targetStock);
		response.setSuggestedReorderQuantity(Math.max(0, targetStock - stock));

		if (stock <= lowStockThreshold) {
			response.setStockStatus("LOW_STOCK");
		} else {
			response.setStockStatus("SUFFICIENT_STOCK");
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
			throw new IllegalArgumentException("Target stock must be greater than the low-stock threshold");
		}
	}
}
