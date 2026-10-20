package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

	public List<Medicine> findByIsDeletedFalse();

	public Optional<Medicine> findByIdAndIsDeletedFalse(Long id);

	public List<Medicine> findByIsActiveTrueAndIsDeletedFalse();

	public boolean existsByBarcodeIgnoreCaseAndIsDeletedFalse(String barcode);

	public boolean existsByBarcodeIgnoreCaseAndIdNotAndIsDeletedFalse(String barcode, Long id);

}
