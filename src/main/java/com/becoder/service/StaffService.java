package com.becoder.service;

import java.util.List;

import com.becoder.dto.StaffRequest;
import com.becoder.dto.StaffResponse;
import com.becoder.dto.StaffUpdateRequest;

public interface StaffService {

	StaffResponse createStaff(StaffRequest request);

    List<StaffResponse> getStaffMembers();
    
    StaffResponse updateStaff(Long staffId, StaffUpdateRequest request);

    void deleteStaff(Long staffId);
	
}
