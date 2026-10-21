package com.becoder.repository;

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

}
