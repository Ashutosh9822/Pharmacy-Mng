package com.becoder.serviceImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.ExpiryBatchResponse;
import com.becoder.entity.Medicine;
import com.becoder.entity.MedicineBatch;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.service.ExpiryTrackingService;

@Service
@Transactional(readOnly = true)
public class ExpiryTrackingServiceImpl implements ExpiryTrackingService {

	@Autowired
	private MedicineBatchRepository batchRepository;

	@Override
	public List<ExpiryBatchResponse> getExpiredBatches() {
		LocalDate today = LocalDate.now();
		return batchRepository.findExpiredBatches(today).stream().map(batch -> toResponse(batch, today)).toList();
	}

	@Override
	public List<ExpiryBatchResponse> getExpiringSoonBatches(int days) {
		if (days < 1 || days > 365) {
			throw new IllegalArgumentException("Days must be between 1 and 365");
		}
		
		LocalDate today = LocalDate.now();
		LocalDate endDate = today.plusDays(days);

		return batchRepository.findBatchesExpiringBetween(today, endDate).stream().map(batch -> toResponse(batch, today)).toList();
	}

	@Override
	public List<ExpiryBatchResponse> getExpiryReport() {
		LocalDate today = LocalDate.now();

		// Include expired batches and batches expiring
		// within the next 90 days.
		LocalDate endDate = today.plusDays(90);

		List<MedicineBatch> expired = batchRepository.findExpiredBatches(today);
		List<MedicineBatch> expiringSoon = batchRepository.findBatchesExpiringBetween(today, endDate);

		return java.util.stream.Stream.concat(expired.stream(), expiringSoon.stream()).map(batch -> toResponse(batch, today)).toList();
	}

	private ExpiryBatchResponse toResponse(MedicineBatch batch, LocalDate today) {

		Medicine medicine = batch.getMedicine();

		long daysUntilExpiry = ChronoUnit.DAYS.between(today, batch.getExpiryDate());

		String status;

		if (daysUntilExpiry < 0) {
			status = "EXPIRED";
		} else if (daysUntilExpiry <= 30) {
			status = "EXPIRING_SOON";
		} else {
			status = "SAFE";
		}

		ExpiryBatchResponse response = new ExpiryBatchResponse();

		response.setBatchId(batch.getId());
		response.setMedicineId(medicine.getId());
		response.setMedicineName(medicine.getName());
		response.setGenericName(medicine.getGenericName());
		response.setBrand(medicine.getBrand());
		response.setBatchNumber(batch.getBatchNumber());
		response.setManufacturingDate(batch.getManufacturingDate());
		response.setExpiryDate(batch.getExpiryDate());
		response.setQuantity(batch.getQuantity());
		response.setPurchasePrice(batch.getPurchasePrice());
		response.setSellingPrice(batch.getSellingPrice());
		response.setDaysUntilExpiry(daysUntilExpiry);
		response.setExpiryStatus(status);

		return response;
	}
}
