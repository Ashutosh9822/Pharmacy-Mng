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

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired	
	private CategoryRepository categoryRepository;

	@Override
	public CategoryResponse createCategory(CategoryRequest request) {

		String name = request.getName().trim();

		if (categoryRepository.existsByNameIgnoreCaseAndIsDeletedFalse(name)) {
			throw new IllegalArgumentException("An active record with this category name already exists");
		}

		Category category = new Category();
		category.setName(name);
		category.setDescription(request.getDescription());

		// New categories are active by default.
		category.setActive(request.getIsActive() == null || request.getIsActive());

		category.setDeleted(false);

		Category saved = categoryRepository.save(category);

		return toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> getAllCategories() {

		return categoryRepository.findByIsDeletedFalse().stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryResponse getCategoryById(Long id) {

		return toResponse(findCategory(id));
	}

	@Override
	public CategoryResponse updateCategory(Long id, CategoryRequest request) {

		Category category = findCategory(id);
		String name = request.getName().trim();

		boolean duplicate = categoryRepository.existsByNameIgnoreCaseAndIsDeletedFalse(name);

		if (duplicate && !category.getName().equalsIgnoreCase(name)) {
			throw new IllegalArgumentException("An active record with this category name already exists");
		}

		category.setName(name);
		category.setDescription(request.getDescription());

		// If omitted, preserve the existing active status.
		if (request.getIsActive() != null) {
			category.setActive(request.getIsActive());
		}

		// JPA's @PreUpdate updates updatedOn.
		Category saved = categoryRepository.save(category);

		return toResponse(saved);
	}

	@Override
	public void deleteCategory(Long id) {

		Category category = findCategory(id);

		// Soft delete: retain the database record.
		category.setDeleted(true);
		category.setActive(false);

		categoryRepository.save(category);
	}

	private Category findCategory(Long id) {
		return categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
	}

	private CategoryResponse toResponse(Category category) {

		return new CategoryResponse(category.getId(), category.getName(), category.getDescription(),
				category.isActive(), category.isDeleted(), category.getCreatedAt(), category.getUpdatedOn(),
				category.getUpdatedBy());
	}

	@Override
	public CategoryResponse restoreCategory(Long id) {
		 Category category = categoryRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new CategoryNotFoundException(
		                    "Deleted category not found with id: " + id
		               )
		          );
		 
		    category.setDeleted(false);
		    category.setActive(true);

		    Category restoredCategory = categoryRepository.save(category);

		    return toResponse(restoredCategory);
	}

	@Override
	public List<CategoryResponse> getActiveCategories() {
		 return categoryRepository.findByIsActiveTrueAndIsDeletedFalse().stream().map(this::toResponse).toList();
	}
	
	
	
}
