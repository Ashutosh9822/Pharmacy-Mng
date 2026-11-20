package com.becoder.service;

import com.becoder.dto.PharmacyProfileRequest;
import com.becoder.dto.PharmacyProfileResponse;

public interface PharmacyService {

	PharmacyProfileResponse getPharmacyProfile();

	PharmacyProfileResponse updatePharmacyProfile(PharmacyProfileRequest request);

}
