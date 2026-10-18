package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.becoder.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByIsDeletedFalse();

	Optional<Category> findByIdAndIsDeletedFalse(Long id);

	boolean existsByNameIgnoreCaseAndIsDeletedFalse(String name);

	Optional<Category> findByIdAndIsDeletedTrue(Long id);
	
	List<Category> findByIsActiveTrueAndIsDeletedFalse();
}
