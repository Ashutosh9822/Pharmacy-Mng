package com.becoder.service;

import java.util.List;

import com.becoder.dto.StockMovementRequest;
import com.becoder.dto.StockMovementResponse;

public interface StockMovementService {

	StockMovementResponse stockIn(StockMovementRequest request);

	StockMovementResponse stockOut(StockMovementRequest request);

	List<StockMovementResponse> getAllMovements();

	List<StockMovementResponse> getMovementsByBatch(Long batchId);

}
