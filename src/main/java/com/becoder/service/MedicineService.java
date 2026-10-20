package com.becoder.service;

import java.util.List;

import com.becoder.dto.MedicineRequest;
import com.becoder.dto.MedicineResponse;

public interface MedicineService {

	public MedicineResponse createMedicine(MedicineRequest request);

	public List<MedicineResponse> getAllMedicines();

	public List<MedicineResponse> getActiveMedicines();

	public MedicineResponse getMedicineById(Long id);

	public MedicineResponse updateMedicine(Long id, MedicineRequest request);

	public void deleteMedicine(Long id);

}
