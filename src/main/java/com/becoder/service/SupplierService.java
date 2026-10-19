package com.becoder.service;

import java.util.List;

import com.becoder.dto.SupplierRequest;
import com.becoder.dto.SupplierResponse;

public interface SupplierService {

	SupplierResponse createSupplier(SupplierRequest request);

	List<SupplierResponse> getAllSuppliers();

	List<SupplierResponse> getActiveSuppliers();

	SupplierResponse getSupplierById(Long id);

	SupplierResponse updateSupplier(Long id, SupplierRequest request);

	void deleteSupplier(Long id);

}
