package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.MedicineBatch;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {

	List<MedicineBatch> findByIsDeletedFalse();

	Optional<MedicineBatch> findByIdAndIsDeletedFalse(Long id);

	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndIsDeletedFalse(Long medicineId, String batchNumber);

	boolean existsByMedicine_IdAndBatchNumberIgnoreCaseAndIdNotAndIsDeletedFalse(Long medicineId, String batchNumber,Long id);

	List<MedicineBatch> findByMedicine_IdAndIsDeletedFalse(Long medicineId);

	List<MedicineBatch> findBySupplier_IdAndIsDeletedFalse(Long supplierId);

}
