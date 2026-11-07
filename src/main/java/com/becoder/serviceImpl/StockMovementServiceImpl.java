package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.StockMovementRequest;
import com.becoder.dto.StockMovementResponse;
import com.becoder.entity.MedicineBatch;
import com.becoder.entity.StockMovement;
import com.becoder.entity.StockMovementType;
import com.becoder.exception.InsufficientStockException;
import com.becoder.exception.MedicineBatchNotFoundException;
import com.becoder.repository.MedicineBatchRepository;
import com.becoder.repository.StockMovementRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.StockMovementService;

@Service
@Transactional
public class StockMovementServiceImpl implements StockMovementService {

	@Autowired
	private MedicineBatchRepository batchRepository;

	@Autowired
	private StockMovementRepository movementRepository;

	@Autowired
	private CurrentUserService currentUserService;

	@Override
	public StockMovementResponse stockIn(StockMovementRequest request) {

		MedicineBatch batch = findBatchForUpdate(request.getBatchId());

		int currentQuantity = batch.getQuantity() == null ? 0 : batch.getQuantity();

		int movementQuantity = request.getQuantity();

		// Prevent integer overflow.
		int newQuantity = Math.addExact(currentQuantity, movementQuantity);

		batch.setQuantity(newQuantity);

		batchRepository.save(batch);

		StockMovement movement = createMovement(batch, request, StockMovementType.STOCK_IN);

		StockMovement savedMovement = movementRepository.save(movement);

		return toResponse(savedMovement, newQuantity);
	}

	@Override
	public StockMovementResponse stockOut(StockMovementRequest request) {

		MedicineBatch batch = findBatchForUpdate(request.getBatchId());

		int currentQuantity = batch.getQuantity() == null ? 0 : batch.getQuantity();

		int movementQuantity = request.getQuantity();

		if (movementQuantity > currentQuantity) {

			throw new InsufficientStockException("Insufficient stock. Available quantity: " + currentQuantity
					+ ", requested quantity: " + movementQuantity);
		}

		int newQuantity = currentQuantity - movementQuantity;

		batch.setQuantity(newQuantity);

		batchRepository.save(batch);

		StockMovement movement = createMovement(batch, request, StockMovementType.STOCK_OUT);

		StockMovement savedMovement = movementRepository.save(movement);

		return toResponse(savedMovement, newQuantity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockMovementResponse> getAllMovements() {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return movementRepository.findByBatch_Pharmacy_IdOrderByCreatedAtDesc(pharmacyId).stream()
				.map(movement -> toResponse(movement, null)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockMovementResponse> getMovementsByBatch(Long batchId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Confirm that the batch belongs to this pharmacy.
		batchRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(batchId, pharmacyId)
				.orElseThrow(() -> new MedicineBatchNotFoundException("Medicine batch not found with id: " + batchId));

		return movementRepository.findByBatch_IdAndBatch_Pharmacy_IdOrderByCreatedAtDesc(batchId, pharmacyId).stream()
				.map(movement -> toResponse(movement, null)).toList();
	}

	private MedicineBatch findBatchForUpdate(Long batchId) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Lock and retrieve only a batch belonging to this pharmacy.
		return batchRepository.findByIdAndPharmacyIdAndIsDeletedFalseForUpdate(batchId, pharmacyId)
				.orElseThrow(() -> new MedicineBatchNotFoundException("Medicine batch not found with id: " + batchId));
	}

	private StockMovement createMovement(MedicineBatch batch, StockMovementRequest request, StockMovementType type) {

		StockMovement movement = new StockMovement();

		movement.setBatch(batch);
		movement.setMovementType(type);
		movement.setQuantity(request.getQuantity());
		movement.setReason(request.getReason());
		movement.setReferenceNumber(request.getReferenceNumber());

		return movement;
	}

	private StockMovementResponse toResponse(StockMovement movement, Integer quantityAfterMovement) {

		StockMovementResponse response = new StockMovementResponse();

		response.setId(movement.getId());
		response.setBatchId(movement.getBatch().getId());
		response.setBatchNumber(movement.getBatch().getBatchNumber());

		response.setMedicineId(movement.getBatch().getMedicine().getId());
		response.setMedicineName(movement.getBatch().getMedicine().getName());

		response.setMovementType(movement.getMovementType());
		response.setQuantity(movement.getQuantity());
		response.setQuantityAfterMovement(quantityAfterMovement);
		response.setReason(movement.getReason());
		response.setReferenceNumber(movement.getReferenceNumber());
		response.setCreatedAt(movement.getCreatedAt());

		return response;
	}
}
