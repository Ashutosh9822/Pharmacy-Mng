package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	// Find all non-deleted suppliers belonging to a pharmacy.
	List<Supplier> findByPharmacy_IdAndIsDeletedFalse(Long pharmacyId);

	// Find a non-deleted supplier by ID within a pharmacy.
	Optional<Supplier> findByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Find active, non-deleted suppliers within a pharmacy.
	List<Supplier> findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(Long pharmacyId);

	// Find a deleted supplier by ID within a pharmacy (for restoration).
	Optional<Supplier> findByIdAndPharmacy_IdAndIsDeletedTrue(Long id, Long pharmacyId);
}
