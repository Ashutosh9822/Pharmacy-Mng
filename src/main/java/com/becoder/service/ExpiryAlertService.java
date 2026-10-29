package com.becoder.service;

import java.util.List;

import com.becoder.dto.ExpiryAlertResponse;

public interface ExpiryAlertService {

	List<ExpiryAlertResponse> getExpiryAlerts(int days);
	
}
