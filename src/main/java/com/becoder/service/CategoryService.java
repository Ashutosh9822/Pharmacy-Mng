package com.becoder.service;

import java.util.List;

import com.becoder.dto.CategoryRequest;
import com.becoder.dto.CategoryResponse;

public interface CategoryService {

	public CategoryResponse createCategory(CategoryRequest request);

	public List<CategoryResponse> getAllCategories();

	public CategoryResponse getCategoryById(Long id);

	public CategoryResponse updateCategory(Long id, CategoryRequest request);

	public void deleteCategory(Long id);

	public CategoryResponse restoreCategory(Long id);
	
	public List<CategoryResponse> getActiveCategories();
}
