package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

	List<StockMovement> findByBatch_IdOrderByCreatedAtDesc(Long batchId);

	List<StockMovement> findAllByOrderByCreatedAtDesc();

}
