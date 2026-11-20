package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.becoder.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

	// Find all non-deleted medicines belonging to a pharmacy.
	List<Medicine> findByPharmacy_IdAndIsDeletedFalse(Long pharmacyId);

	// Find a medicine by ID within a pharmacy.
	Optional<Medicine> findByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Find active, non-deleted medicines within a pharmacy.
	List<Medicine> findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(Long pharmacyId);

	// Check barcode duplication within the same pharmacy.
	boolean existsByBarcodeIgnoreCaseAndPharmacy_IdAndIsDeletedFalse(String barcode, Long pharmacyId);

	// Check barcode duplication during update, excluding the current medicine.
	boolean existsByBarcodeIgnoreCaseAndIdNotAndPharmacy_IdAndIsDeletedFalse(String barcode, Long id, Long pharmacyId);

	@Query("""
			SELECT m FROM Medicine m
			WHERE m.pharmacy.id = :pharmacyId
			  AND m.isActive = true
			  AND m.isDeleted = false
			  AND (
			      LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(COALESCE(m.genericName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(COALESCE(m.brand, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(COALESCE(m.barcode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
			  )
			ORDER BY m.name ASC
			""")
	List<Medicine> searchForBilling(@Param("pharmacyId") Long pharmacyId, @Param("keyword") String keyword);

}
