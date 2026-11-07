package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.CategoryRequest;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.CategoryNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;
import com.becoder.service.CurrentUserService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        String name = request.getName().trim();

        // Check duplicate category name within the current pharmacy
        if (categoryRepository.existsByNameIgnoreCaseAndPharmacy_Id(request.getName().trim(), pharmacyId)) {
            throw new IllegalArgumentException("An active record with this category name already exists");
        }

        Category category = new Category();

        category.setName(name);
        category.setDescription(request.getDescription());

        // Automatically assign the authenticated user's pharmacy
        category.setPharmacy(currentUserService.getCurrentUser().getPharmacy());

        // New categories are active by default
        category.setActive(request.getIsActive() == null || request.getIsActive());

        category.setDeleted(false);

        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        return categoryRepository
                .findByPharmacy_IdAndIsDeletedFalse(pharmacyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(findCategory(id));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = findCategory(id);

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        String name = request.getName().trim();

        boolean duplicate = categoryRepository.existsByNameIgnoreCaseAndPharmacy_Id(request.getName().trim(), pharmacyId);

        if (duplicate && !category.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("An active record with this category name already exists");
        }

        category.setName(name);
        category.setDescription(request.getDescription());

        // If omitted, preserve the existing active status
        if (request.getIsActive() != null) {
            category.setActive(request.getIsActive());
        }

        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = findCategory(id);

        // Soft delete: retain the database record
        category.setDeleted(true);
        category.setActive(false);

        categoryRepository.save(category);
    }

    private Category findCategory(Long id) {

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        return categoryRepository
                .findByIdAndPharmacy_IdAndIsDeletedFalse(id, pharmacyId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public CategoryResponse restoreCategory(Long id) {

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        Category category = categoryRepository
                .findByIdAndPharmacy_IdAndIsDeletedTrue(id, pharmacyId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Deleted category not found with id: " + id));

        category.setDeleted(false);
        category.setActive(true);

        Category restoredCategory = categoryRepository.save(category);

        return toResponse(restoredCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {

        Long pharmacyId = currentUserService.getCurrentPharmacyId();

        return categoryRepository
                .findByPharmacy_IdAndIsActiveTrueAndIsDeletedFalse(pharmacyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.isDeleted(),
                category.getCreatedAt(),
                category.getUpdatedOn(),
                category.getUpdatedBy());
    }
}
