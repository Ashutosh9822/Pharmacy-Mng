package com.becoder.service;

import java.util.List;

import com.becoder.dto.MedicineBatchRequest;
import com.becoder.dto.MedicineBatchResponse;

public interface MedicineBatchService {

	public MedicineBatchResponse createBatch(MedicineBatchRequest request);

	public List<MedicineBatchResponse> getAllBatches();

	public MedicineBatchResponse getBatchById(Long id);

	public MedicineBatchResponse updateBatch(Long id, MedicineBatchRequest request);

	public void deleteBatch(Long id);

	public List<MedicineBatchResponse> getBatchesByMedicine(Long medicineId);

	public List<MedicineBatchResponse> getBatchesBySupplier(Long supplierId);

}
