package com.becoder.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.MedicineBatchRequest;
import com.becoder.dto.MedicineBatchResponse;
import com.becoder.entity.Medicine;
import com.becoder.entity.MedicineBatch;
import com.becoder.entity.Supplier;
import com.becoder.exception.DuplicateMedicineBatchException;
import com.becoder.exception.MedicineBatchNotFoundException;
import com.becoder.exception.MedicineNotFoundException;
import com.becoder.exception.SupplierNotFoundException;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.repository.MedicineRepository;
import com.becoder.repository.SupplierRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.MedicineBatchService;

@Service
@Transactional
public class MedicineBatchServiceImpl implements MedicineBatchService {

	@Autowired
	private MedicineBatchRepository batchRepository;

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private CurrentUserService currentUserService;

	@Override
	public MedicineBatchResponse createBatch(MedicineBatchRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Verify medicine belongs to the authenticated pharmacy
		Medicine medicine = findActiveMedicine(request.getMedicineId(), pharmacyId);

		// Verify supplier belongs to the authenticated pharmacy
		Supplier supplier = findActiveSupplier(request.getSupplierId(), pharmacyId);

		validateDates(request.getManufacturingDate(), request.getExpiryDate());

		validatePrices(request.getPurchasePrice(), request.getSellingPrice());

		String batchNumber = request.getBatchNumber().trim();

		boolean duplicate = batchRepository.existsByMedicine_IdAndBatchNumberIgnoreCaseAndPharmacy_IdAndIsDeletedFalse(
				medicine.getId(), batchNumber, pharmacyId);

		if (duplicate) {
			throw new DuplicateMedicineBatchException(
					"Batch number '" + batchNumber + "' already exists for this medicine in this pharmacy");
		}

		MedicineBatch batch = new MedicineBatch();

		batch.setPharmacy(currentUserService.getCurrentUser().getPharmacy());
		batch.setMedicine(medicine);
		batch.setSupplier(supplier);
		batch.setBatchNumber(batchNumber);
		batch.setQuantity(request.getQuantity());
		batch.setManufacturingDate(request.getManufacturingDate());
		batch.setExpiryDate(request.getExpiryDate());
		batch.setPurchasePrice(request.getPurchasePrice());
		batch.setSellingPrice(request.getSellingPrice());
		batch.setDeleted(false);

		return toResponse(batchRepository.save(batch));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicineBatchResponse> getAllBatches() {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return batchRepository.findByPharmacy_IdAndIsDeletedFalse(pharmacyId).stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public MedicineBatchResponse getBatchById(Long id) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return toResponse(findExistingBatch(id, pharmacyId));
	}

	@Override
	public MedicineBatchResponse updateBatch(Long id, MedicineBatchRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Verify batch belongs to the authenticated pharmacy
		MedicineBatch batch = findExistingBatch(id, pharmacyId);

		// Verify medicine and supplier ownership
		Medicine medicine = findActiveMedicine(request.getMedicineId(), pharmacyId);

		Supplier supplier = findActiveSupplier(request.getSupplierId(), pharmacyId);

		validateDates(request.getManufacturingDate(), request.getExpiryDate());

		validatePrices(request.getPurchasePrice(), request.getSellingPrice());

		String batchNumber = request.getBatchNumber().trim();

		boolean duplicate = batchRepository
				.existsByMedicine_IdAndBatchNumberIgnoreCaseAndIdNotAndPharmacy_IdAndIsDeletedFalse(medicine.getId(),
						batchNumber, id, pharmacyId);

		if (duplicate) {
			throw new DuplicateMedicineBatchException(
					"Batch number '" + batchNumber + "' already exists for this medicine in this pharmacy");
		}

		batch.setMedicine(medicine);
		batch.setSupplier(supplier);
		batch.setBatchNumber(batchNumber);
		batch.setQuantity(request.getQuantity());
		batch.setManufacturingDate(request.getManufacturingDate());
		batch.setExpiryDate(request.getExpiryDate());
		batch.setPurchasePrice(request.getPurchasePrice());
		batch.setSellingPrice(request.getSellingPrice());

		return toResponse(batchRepository.save(batch));
	}

	@Override
	public void deleteBatch(Long id) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		MedicineBatch batch = findExistingBatch(id, pharmacyId);

		batch.setDeleted(true);

		batchRepository.save(batch);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicineBatchResponse> getBatchesByMedicine(Long medicineId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		findActiveMedicine(medicineId, pharmacyId);

		return batchRepository.findByMedicine_IdAndPharmacy_IdAndIsDeletedFalse(medicineId, pharmacyId).stream()
				.map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicineBatchResponse> getBatchesBySupplier(Long supplierId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		findActiveSupplier(supplierId, pharmacyId);

		return batchRepository.findBySupplier_IdAndPharmacy_IdAndIsDeletedFalse(supplierId, pharmacyId).stream()
				.map(this::toResponse).toList();
	}

	// Find a batch belonging to the current pharmacy
	private MedicineBatch findExistingBatch(Long id, Long pharmacyId) {

		return batchRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(id, pharmacyId)
				.orElseThrow(() -> new MedicineBatchNotFoundException("Medicine batch not found with id: " + id));
	}

	// Find an active medicine belonging to the current pharmacy
	private Medicine findActiveMedicine(Long medicineId, Long pharmacyId) {

		return medicineRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(medicineId, pharmacyId)
				.filter(Medicine::isActive)
				.orElseThrow(() -> new MedicineNotFoundException("Active medicine not found with id: " + medicineId));
	}

	// Find an active supplier belonging to the current pharmacy
	private Supplier findActiveSupplier(Long supplierId, Long pharmacyId) {

		return supplierRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(supplierId, pharmacyId)
				.filter(Supplier::isActive)
				.orElseThrow(() -> new SupplierNotFoundException("Active supplier not found with id: " + supplierId));
	}

	private void validateDates(LocalDate manufacturingDate, LocalDate expiryDate) {

		if (expiryDate == null) {
			throw new IllegalArgumentException("Expiry date is required");
		}

		if (manufacturingDate != null && manufacturingDate.isAfter(expiryDate)) {

			throw new IllegalArgumentException("Manufacturing date cannot be after expiry date");
		}
	}

	private void validatePrices(BigDecimal purchasePrice, BigDecimal sellingPrice) {

		if (purchasePrice == null || sellingPrice == null) {
			throw new IllegalArgumentException("Purchase price and selling price are required");
		}

		if (purchasePrice.signum() < 0 || sellingPrice.signum() < 0) {
			throw new IllegalArgumentException("Prices cannot be negative");
		}
	}

	private MedicineBatchResponse toResponse(MedicineBatch batch) {

		return new MedicineBatchResponse(batch.getId(), batch.getMedicine().getId(), batch.getMedicine().getName(),
				batch.getSupplier().getId(), batch.getSupplier().getName(), batch.getBatchNumber(), batch.getQuantity(),
				batch.getManufacturingDate(), batch.getExpiryDate(), batch.getPurchasePrice(), batch.getSellingPrice(),
				batch.isDeleted(), batch.getCreatedAt(), batch.getUpdatedOn(), batch.getUpdatedBy());
	}
}
