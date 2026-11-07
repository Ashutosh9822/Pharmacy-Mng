
package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

	// Get movements for a particular batch belonging to a pharmacy.
	List<StockMovement> findByBatch_IdAndBatch_Pharmacy_IdOrderByCreatedAtDesc(Long batchId, Long pharmacyId);

	// Get all movements belonging to a particular pharmacy.
	List<StockMovement> findByBatch_Pharmacy_IdOrderByCreatedAtDesc(Long pharmacyId);
}
