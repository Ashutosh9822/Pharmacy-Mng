package com.becoder.service;

import java.util.List;

import com.becoder.dto.StaffRequest;
import com.becoder.dto.StaffResponse;

public interface StaffService {

	StaffResponse createStaff(StaffRequest request);

    List<StaffResponse> getStaffMembers();
	
}
