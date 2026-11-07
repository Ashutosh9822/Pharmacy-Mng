
package com.becoder.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.becoder.entity.MedicineBatch;

import jakarta.persistence.LockModeType;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {

	// Get all non-deleted batches belonging to a pharmacy
	List<MedicineBatch> findByPharmacy_IdAndIsDeletedFalse(Long pharmacyId);

	// Get a specific non-deleted batch belonging to a pharmacy
	Optional<MedicineBatch> findByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Check duplicate batch number within a pharmacy
	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndPharmacy_IdAndIsDeletedFalse(Long medicineId,
			String batchNumber, Long pharmacyId);

	// Check duplicate batch number while excluding current batch
	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndIdNotAndPharmacy_IdAndIsDeletedFalse(Long medicineId,
			String batchNumber, Long id, Long pharmacyId);

	// Get batches for a specific medicine within a pharmacy
	List<MedicineBatch> findByMedicine_IdAndPharmacy_IdAndIsDeletedFalse(Long medicineId, Long pharmacyId);

	// Get batches for a specific supplier within a pharmacy
	List<MedicineBatch> findBySupplier_IdAndPharmacy_IdAndIsDeletedFalse(Long supplierId, Long pharmacyId);

	// Lock a pharmacy-owned batch for update
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT b FROM MedicineBatch b
			WHERE b.id = :id
			  AND b.pharmacy.id = :pharmacyId
			  AND b.isDeleted = false
			""")
	Optional<MedicineBatch> findByIdAndPharmacyIdAndIsDeletedFalseForUpdate(@Param("id") Long id,
			@Param("pharmacyId") Long pharmacyId);

	// Find expired batches belonging to a pharmacy
	@Query("""
			SELECT b FROM MedicineBatch b
			WHERE b.pharmacy.id = :pharmacyId
			  AND b.isDeleted = false
			  AND b.expiryDate < :today
			ORDER BY b.expiryDate ASC
			""")
	List<MedicineBatch> findExpiredBatches(@Param("today") LocalDate today, @Param("pharmacyId") Long pharmacyId);

	// Find batches expiring between today and a specified date
	@Query("""
			SELECT b FROM MedicineBatch b
			WHERE b.pharmacy.id = :pharmacyId
			  AND b.isDeleted = false
			  AND b.expiryDate >= :today
			  AND b.expiryDate <= :endDate
			ORDER BY b.expiryDate ASC
			""")
	List<MedicineBatch> findBatchesExpiringBetween(@Param("today") LocalDate today, @Param("endDate") LocalDate endDate,
			@Param("pharmacyId") Long pharmacyId);

	// Find usable stock grouped by medicine within a pharmacy
	@Query("""
			SELECT b.medicine.id, COALESCE(SUM(b.quantity), 0)
			FROM MedicineBatch b
			WHERE b.pharmacy.id = :pharmacyId
			  AND b.isDeleted = false
			  AND b.expiryDate >= :today
			  AND b.medicine.isDeleted = false
			  AND b.medicine.isActive = true
			GROUP BY b.medicine.id
			""")
	List<Object[]> findUsableStockByMedicine(@Param("today") LocalDate today, @Param("pharmacyId") Long pharmacyId);

	// Find usable stock grouped by supplier and medicine
	@Query("""
			SELECT
			    b.supplier.id,
			    b.supplier.name,
			    b.medicine.id,
			    b.medicine.name,
			    b.medicine.genericName,
			    b.medicine.brand,
			    COALESCE(SUM(b.quantity), 0),
			    COALESCE(SUM(b.quantity * b.purchasePrice), 0)
			FROM MedicineBatch b
			WHERE b.pharmacy.id = :pharmacyId
			  AND b.isDeleted = false
			  AND b.expiryDate >= :today
			  AND b.medicine.isDeleted = false
			  AND b.medicine.isActive = true
			  AND b.supplier.isDeleted = false
			  AND b.supplier.isActive = true
			GROUP BY
			    b.supplier.id,
			    b.supplier.name,
			    b.medicine.id,
			    b.medicine.name,
			    b.medicine.genericName,
			    b.medicine.brand
			ORDER BY b.supplier.name ASC, b.medicine.name ASC
			""")
	List<Object[]> findUsableStockBySupplierAndMedicine(@Param("today") LocalDate today,
			@Param("pharmacyId") Long pharmacyId);

}
