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

	List<MedicineBatch> findByIsDeletedFalse();

	Optional<MedicineBatch> findByIdAndIsDeletedFalse(Long id);

	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndIsDeletedFalse(Long medicineId, String batchNumber);

	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndIdNotAndIsDeletedFalse(Long medicineId, String batchNumber,Long id);

	List<MedicineBatch> findByMedicine_IdAndIsDeletedFalse(Long medicineId);

	List<MedicineBatch> findBySupplier_IdAndIsDeletedFalse(Long supplierId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT b FROM MedicineBatch b WHERE b.id = :id AND b.isDeleted = false")
	Optional<MedicineBatch> findByIdAndIsDeletedFalseForUpdate(@Param("id") Long id);

	// Find batches that have already expired
	@Query("""
			SELECT b FROM MedicineBatch b
			WHERE b.isDeleted = false
			  AND b.expiryDate < :today
			ORDER BY b.expiryDate ASC
			""")
	List<MedicineBatch> findExpiredBatches(@Param("today") LocalDate today);

	// Find batches expiring between today and a specified date
	@Query("""
			SELECT b FROM MedicineBatch b
			WHERE b.isDeleted = false
			  AND b.expiryDate >= :today
			  AND b.expiryDate <= :endDate
			ORDER BY b.expiryDate ASC
			""")
	List<MedicineBatch> findBatchesExpiringBetween(@Param("today") LocalDate today,@Param("endDate") LocalDate endDate);

	@Query("""
			SELECT b.medicine.id, COALESCE(SUM(b.quantity), 0)
			FROM MedicineBatch b
			WHERE b.isDeleted = false
			  AND b.expiryDate >= :today
			  AND b.medicine.isDeleted = false
			  AND b.medicine.isActive = true
			GROUP BY b.medicine.id
			""")
	List<Object[]> findUsableStockByMedicine(@Param("today") LocalDate today);

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
			WHERE b.isDeleted = false
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
	List<Object[]> findUsableStockBySupplierAndMedicine(@Param("today") LocalDate today);

}
