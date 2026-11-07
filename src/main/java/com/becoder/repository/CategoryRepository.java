package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	// Get all non-deleted categories of a pharmacy
	List<Category> findByPharmacy_IdAndIsDeletedFalse(Long pharmacyId);

	// Get a specific non-deleted category belonging to a pharmacy
	Optional<Category> findByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Check duplicate category name, including soft-deleted categories
	boolean existsByNameIgnoreCaseAndPharmacy_Id(String name, Long pharmacyId);

	// Check duplicate category name during update, excluding the current category
	boolean existsByNameIgnoreCaseAndIdNotAndPharmacy_Id(String name, Long id, Long pharmacyId);

	// Find a deleted category belonging to a pharmacy (for restoration)
	Optional<Category> findByIdAndPharmacy_IdAndIsDeletedTrue(Long id, Long pharmacyId);

	// Get active and non-deleted categories of a pharmacy
	List<Category> findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(Long pharmacyId);
}
