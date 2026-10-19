package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	// Find all suppliers that have not been deleted.
    List<Supplier> findByIsDeletedFalse();

    // Find one supplier only if it has not been deleted.
    Optional<Supplier> findByIdAndIsDeletedFalse(Long id);

    // Find active suppliers for ordering and replenishment.
    List<Supplier> findByIsActiveTrueAndIsDeletedFalse();
	
}
