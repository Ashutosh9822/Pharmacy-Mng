package com.becoder.serviceImpl;

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

    @Override
    public MedicineBatchResponse createBatch(MedicineBatchRequest request) {

        Medicine medicine = findActiveMedicine(request.getMedicineId());
        Supplier supplier = findActiveSupplier(request.getSupplierId());

        validateDates(request.getManufacturingDate(), request.getExpiryDate());
        validatePrices(request.getPurchasePrice(), request.getSellingPrice());

        String batchNumber = request.getBatchNumber().trim();

        if (batchRepository
                .existsByMedicine_IdAndBatchNumberIgnoreCaseAndIsDeletedFalse(
                        medicine.getId(), batchNumber)) {
            throw new DuplicateMedicineBatchException(
                    "Batch number '" + batchNumber
                            + "' already exists for this medicine");
        }

        MedicineBatch batch = new MedicineBatch();
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
        return batchRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineBatchResponse getBatchById(Long id) {
        return toResponse(findExistingBatch(id));
    }

    @Override
    public MedicineBatchResponse updateBatch(
            Long id, MedicineBatchRequest request) {

        MedicineBatch batch = findExistingBatch(id);

        Medicine medicine = findActiveMedicine(request.getMedicineId());
        Supplier supplier = findActiveSupplier(request.getSupplierId());

        validateDates(request.getManufacturingDate(), request.getExpiryDate());
        validatePrices(request.getPurchasePrice(), request.getSellingPrice());

        String batchNumber = request.getBatchNumber().trim();

        boolean duplicate = batchRepository
                .existsByMedicine_IdAndBatchNumberIgnoreCaseAndIdNotAndIsDeletedFalse(
                        medicine.getId(), batchNumber, id);

        if (duplicate) {
            throw new DuplicateMedicineBatchException(
                    "Batch number '" + batchNumber + "' already exists for this medicine");
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
        MedicineBatch batch = findExistingBatch(id);
        batch.setDeleted(true);
        batchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponse> getBatchesByMedicine(Long medicineId) {
        findActiveMedicine(medicineId);
        return batchRepository.findByMedicine_IdAndIsDeletedFalse(medicineId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponse> getBatchesBySupplier(Long supplierId) {
        findActiveSupplier(supplierId);
        return batchRepository.findBySupplier_IdAndIsDeletedFalse(supplierId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicineBatch findExistingBatch(Long id) {
        return batchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new MedicineBatchNotFoundException("Medicine batch not found with id: " + id));
    }

    private Medicine findActiveMedicine(Long medicineId) {
        return medicineRepository.findByIdAndIsDeletedFalse(medicineId)
                .filter(Medicine::isActive)
                .orElseThrow(() -> new MedicineNotFoundException(
                        "Active medicine not found with id: " + medicineId));
    }

    private Supplier findActiveSupplier(Long supplierId) {
        return supplierRepository.findByIdAndIsDeletedFalse(supplierId)
                .filter(Supplier::isActive)
                .orElseThrow(() -> new SupplierNotFoundException(
                        "Active supplier not found with id: " + supplierId));
    }

    private void validateDates(LocalDate manufacturingDate, LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new IllegalArgumentException("Expiry date is required");
        }

        if (manufacturingDate != null
                && manufacturingDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException(
                    "Manufacturing date cannot be after expiry date");
        }
    }

    private void validatePrices(java.math.BigDecimal purchasePrice,java.math.BigDecimal sellingPrice) {
        if (purchasePrice == null || sellingPrice == null) {
            throw new IllegalArgumentException(
                    "Purchase price and selling price are required");
        }

        if (purchasePrice.signum() < 0 || sellingPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "Prices cannot be negative");
        }
    }

    private MedicineBatchResponse toResponse(MedicineBatch batch) {

        return new MedicineBatchResponse(
                batch.getId(),
                batch.getMedicine().getId(),
                batch.getMedicine().getName(),
                batch.getSupplier().getId(),
                batch.getSupplier().getName(),
                batch.getBatchNumber(),
                batch.getQuantity(),
                batch.getManufacturingDate(),
                batch.getExpiryDate(),
                batch.getPurchasePrice(),
                batch.getSellingPrice(),
                batch.isDeleted(),
                batch.getCreatedAt(),
                batch.getUpdatedOn(),
                batch.getUpdatedBy());
    }
}
