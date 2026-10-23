package com.becoder.service;

import java.util.List;

import com.becoder.dto.SupplierReorderResponse;

public interface SupplierReorderService {

	List<SupplierReorderResponse> getReorderRecommendations(int lowStockThreshold, int targetStock);

	List<SupplierReorderResponse> getReorderRecommendationsBySupplier(Long supplierId, int lowStockThreshold,int targetStock);

}
