package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.becoder.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	// Get all non-deleted customers belonging to a pharmacy
	List<Customer> findByPharmacy_IdAndIsDeletedFalse(Long pharmacyId);

	// Get one customer belonging to a pharmacy
	Optional<Customer> findByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Check whether a customer exists within a pharmacy
	boolean existsByIdAndPharmacy_IdAndIsDeletedFalse(Long id, Long pharmacyId);

	// Search customers by name within a pharmacy
	List<Customer> findByPharmacy_IdAndFullNameContainingIgnoreCaseAndIsDeletedFalse(Long pharmacyId, String fullName);

	// Search customers by phone within a pharmacy
	Optional<Customer> findByPharmacy_IdAndPhoneAndIsDeletedFalse(Long pharmacyId, String phone);
}
