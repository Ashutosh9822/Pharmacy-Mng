package com.becoder.service;

import java.util.List;

import com.becoder.dto.ExpiryBatchResponse;

public interface ExpiryTrackingService {

	List<ExpiryBatchResponse> getExpiredBatches();

	List<ExpiryBatchResponse> getExpiringSoonBatches(int days);

	List<ExpiryBatchResponse> getExpiryReport();

}
