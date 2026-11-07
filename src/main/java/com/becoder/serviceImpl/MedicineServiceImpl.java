package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.becoder.dto.MedicineRequest;
import com.becoder.dto.MedicineResponse;
import com.becoder.entity.Category;
import com.becoder.entity.Medicine;
import com.becoder.exception.CategoryNotFoundException;
import com.becoder.exception.DuplicateBarcodeException;
import com.becoder.exception.MedicineNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.MedicineRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.MedicineService;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CurrentUserService currentUserService;

	// 1. Create medicine
	@Override
	public MedicineResponse createMedicine(MedicineRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		// Verify category belongs to the current pharmacy.
		Category category = findActiveCategory(request.getCategoryId(), pharmacyId);

		// Check barcode duplication within the current pharmacy.
		validateBarcode(request.getBarcode(), null, pharmacyId);

		Medicine medicine = new Medicine();

		medicine.setName(request.getName().trim());
		medicine.setGenericName(request.getGenericName());
		medicine.setBrand(request.getBrand());
		medicine.setDosageForm(request.getDosageForm());
		medicine.setStrength(request.getStrength());
		medicine.setBarcode(normalizeBarcode(request.getBarcode()));
		medicine.setDescription(request.getDescription());
		medicine.setCategory(category);

		// Automatically assign authenticated user's pharmacy.
		medicine.setPharmacy(currentUserService.getCurrentUser().getPharmacy());

		medicine.setActive(request.getIsActive() == null || request.getIsActive());

		medicine.setDeleted(false);

		Medicine savedMedicine = medicineRepository.save(medicine);

		return toResponse(savedMedicine);
	}

	// 2. Get all non-deleted medicines for current pharmacy.
	@Override
	@Transactional(readOnly = true)
	public List<MedicineResponse> getAllMedicines() {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return medicineRepository.findByPharmacy_IdAndIsDeletedFalse(pharmacyId).stream().map(this::toResponse)
				.toList();
	}

	// 3. Get active medicines for current pharmacy.
	@Override
	@Transactional(readOnly = true)
	public List<MedicineResponse> getActiveMedicines() {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return medicineRepository.findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(pharmacyId).stream()
				.map(this::toResponse).toList();
	}

	// 4. Get medicine by ID within current pharmacy.
	@Override
	@Transactional(readOnly = true)
	public MedicineResponse getMedicineById(Long id) {

		Medicine medicine = findExistingMedicine(id);

		return toResponse(medicine);
	}

	// 5. Update medicine within current pharmacy.
	@Override
	public MedicineResponse updateMedicine(Long id, MedicineRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		Medicine medicine = findExistingMedicine(id);

		// Verify the new category belongs to the same pharmacy.
		Category category = findActiveCategory(request.getCategoryId(), pharmacyId);

		// Check barcode duplication within the current pharmacy.
		validateBarcode(request.getBarcode(), id, pharmacyId);

		medicine.setName(request.getName().trim());
		medicine.setGenericName(request.getGenericName());
		medicine.setBrand(request.getBrand());
		medicine.setDosageForm(request.getDosageForm());
		medicine.setStrength(request.getStrength());
		medicine.setBarcode(normalizeBarcode(request.getBarcode()));
		medicine.setDescription(request.getDescription());
		medicine.setCategory(category);

		if (request.getIsActive() != null) {
			medicine.setActive(request.getIsActive());
		}

		Medicine updatedMedicine = medicineRepository.save(medicine);

		return toResponse(updatedMedicine);
	}

	// 6. Soft-delete medicine within current pharmacy.
	@Override
	public void deleteMedicine(Long id) {

		Medicine medicine = findExistingMedicine(id);

		medicine.setDeleted(true);
		medicine.setActive(false);

		medicineRepository.save(medicine);
	}

	// Find only non-deleted medicines belonging to current pharmacy.
	private Medicine findExistingMedicine(Long id) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		return medicineRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(id, pharmacyId)
				.orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + id));
	}

	// Find an active category belonging to current pharmacy.
	private Category findActiveCategory(Long categoryId, Long pharmacyId) {

		return categoryRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(categoryId, pharmacyId)
				.filter(Category::isActive)
				.orElseThrow(() -> new CategoryNotFoundException("Active category not found with id: " + categoryId));
	}

	// Validate barcode uniqueness within current pharmacy.
	private void validateBarcode(String barcode, Long medicineId, Long pharmacyId) {

		if (!StringUtils.hasText(barcode)) {
			return;
		}

		String normalizedBarcode = barcode.trim();

		boolean exists;

		if (medicineId == null) {
			exists = medicineRepository.existsByBarcodeIgnoreCaseAndPharmacy_IdAndIsDeletedFalse(normalizedBarcode,pharmacyId);

		} else {
			exists = medicineRepository.existsByBarcodeIgnoreCaseAndIdNotAndPharmacy_IdAndIsDeletedFalse(normalizedBarcode, medicineId, pharmacyId);
		}

		if (exists) {
			throw new DuplicateBarcodeException("A medicine with barcode '" + normalizedBarcode + "' already exists in this pharmacy");
		}
	}

	// Normalize barcode.
	private String normalizeBarcode(String barcode) {

		if (!StringUtils.hasText(barcode)) {
			return null;
		}

		return barcode.trim();
	}

	// Convert entity to response DTO.
	private MedicineResponse toResponse(Medicine medicine) {

		return new MedicineResponse(medicine.getId(), medicine.getName(), medicine.getGenericName(),
				medicine.getBrand(), medicine.getDosageForm(), medicine.getStrength(), medicine.getBarcode(),
				medicine.getDescription(), medicine.getCategory().getId(), medicine.getCategory().getName(),
				medicine.isActive(), medicine.isDeleted(), medicine.getCreatedAt(), medicine.getUpdatedOn(),
				medicine.getUpdatedBy());
	}
}
