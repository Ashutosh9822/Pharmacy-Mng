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
import com.becoder.service.MedicineService;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

	@Autowired
    private MedicineRepository medicineRepository;
    
	@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public MedicineResponse createMedicine(MedicineRequest request) {

        Category category = findActiveCategory(request.getCategoryId());

        validateBarcode(request.getBarcode(), null);

        Medicine medicine = new Medicine();

        medicine.setName(request.getName().trim());
        medicine.setGenericName(request.getGenericName());
        medicine.setBrand(request.getBrand());
        medicine.setDosageForm(request.getDosageForm());
        medicine.setStrength(request.getStrength());
        medicine.setBarcode(normalizeBarcode(request.getBarcode()));
        medicine.setDescription(request.getDescription());
        medicine.setCategory(category);
        medicine.setActive(request.getIsActive() == null || request.getIsActive());
        medicine.setDeleted(false);

        Medicine savedMedicine = medicineRepository.save(medicine);

        return toResponse(savedMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicines() {

        return medicineRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getActiveMedicines() {

        return medicineRepository.findByIsActiveTrueAndIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {

        Medicine medicine = findExistingMedicine(id);

        return toResponse(medicine);
    }

    @Override
    public MedicineResponse updateMedicine(
            Long id, MedicineRequest request) {

        Medicine medicine = findExistingMedicine(id);

        Category category = findActiveCategory(request.getCategoryId());

        validateBarcode(request.getBarcode(), id);

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

    @Override
    public void deleteMedicine(Long id) {

        Medicine medicine = findExistingMedicine(id);

        medicine.setDeleted(true);
        medicine.setActive(false);

        medicineRepository.save(medicine);
    }

    private Medicine findExistingMedicine(Long id) {

        return medicineRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new MedicineNotFoundException(
                                "Medicine not found with id: " + id));
    }

    private Category findActiveCategory(Long categoryId) {

        return categoryRepository
                .findByIdAndIsDeletedFalse(categoryId)
                .filter(Category::isActive)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Active category not found with id: "+ categoryId));
    }

    private void validateBarcode(String barcode, Long medicineId) {

        if (!StringUtils.hasText(barcode)) {
            return;
        }

        String normalizedBarcode = barcode.trim();

        boolean exists;

        if (medicineId == null) {
            exists = medicineRepository
                    .existsByBarcodeIgnoreCaseAndIsDeletedFalse(
                            normalizedBarcode);
        } else {
            exists = medicineRepository
                    .existsByBarcodeIgnoreCaseAndIdNotAndIsDeletedFalse(
                            normalizedBarcode, medicineId);
        }

        if (exists) {
            throw new DuplicateBarcodeException(
                    "A medicine with barcode '" + normalizedBarcode
                            + "' already exists");
        }
    }

    private String normalizeBarcode(String barcode) {

        if (!StringUtils.hasText(barcode)) {
            return null;
        }

        return barcode.trim();
    }

    private MedicineResponse toResponse(Medicine medicine) {

        return new MedicineResponse(
                medicine.getId(),
                medicine.getName(),
                medicine.getGenericName(),
                medicine.getBrand(),
                medicine.getDosageForm(),
                medicine.getStrength(),
                medicine.getBarcode(),
                medicine.getDescription(),
                medicine.getCategory().getId(),
                medicine.getCategory().getName(),
                medicine.isActive(),
                medicine.isDeleted(),
                medicine.getCreatedAt(),
                medicine.getUpdatedOn(),
                medicine.getUpdatedBy());
    }
}
