
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.CategoryRequest;
import com.becoder.dto.CategoryResponse;
import com.becoder.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/category")
@Validated
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// Create category
	@PostMapping("/create-category")
	public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
		CategoryResponse response = categoryService.createCategory(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get all categories
	@GetMapping("/get-categories")
	public ResponseEntity<List<CategoryResponse>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	// Get category by ID
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	// Update category
	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryRequest request) {
		return ResponseEntity.ok(categoryService.updateCategory(id, request));
	}

	// Delete category
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
	
	// Restore category
	@PutMapping("/{id}/restore")
	public ResponseEntity<CategoryResponse> restoreCategory(@PathVariable Long id) {
	    CategoryResponse response = categoryService.restoreCategory(id);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
	    List<CategoryResponse> categories = categoryService.getActiveCategories();
	    return ResponseEntity.ok(categories);
	}
	
}
