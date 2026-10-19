
package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.SupplierRequest;
import com.becoder.dto.SupplierResponse;
import com.becoder.entity.Supplier;
import com.becoder.exception.SupplierNotFoundException;
import com.becoder.repository.SupplierRepository;
import com.becoder.service.SupplierService;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierRepository supplierRepository;

	// 1. Create supplier
	@Override
	public SupplierResponse createSupplier(SupplierRequest request) {

		Supplier supplier = new Supplier();

		supplier.setName(request.getName().trim());
		supplier.setContactPerson(request.getContactPerson());
		supplier.setEmail(request.getEmail());
		supplier.setPhone(request.getPhone());
		supplier.setAddress(request.getAddress());

		// Default lead time is 7 days.
		supplier.setLeadTimeDays(request.getLeadTimeDays() == null ? 7 : request.getLeadTimeDays());

		// Default active status is true.
		supplier.setActive(request.getIsActive() == null || request.getIsActive());

		supplier.setDeleted(false);

		Supplier savedSupplier = supplierRepository.save(supplier);

		return toResponse(savedSupplier);
	}

	// 2. Get all non-deleted suppliers
	@Override
	@Transactional(readOnly = true)
	public List<SupplierResponse> getAllSuppliers() {
		return supplierRepository.findByIsDeletedFalse().stream().map(this::toResponse).toList();
	}

	// 3. Get active, non-deleted suppliers
	@Override
	@Transactional(readOnly = true)
	public List<SupplierResponse> getActiveSuppliers() {
		return supplierRepository.findByIsActiveTrueAndIsDeletedFalse().stream().map(this::toResponse).toList();
	}

	// 4. Get supplier by ID
	@Override
	@Transactional(readOnly = true)
	public SupplierResponse getSupplierById(Long id) {
		Supplier supplier = findExistingSupplier(id);
		return toResponse(supplier);
	}

	// 5. Update supplier
	@Override
	public SupplierResponse updateSupplier(Long id, SupplierRequest request) {

		Supplier supplier = findExistingSupplier(id);

		supplier.setName(request.getName().trim());

		// Update optional fields only when supplied.
		if (request.getContactPerson() != null) {
			supplier.setContactPerson(request.getContactPerson());
		}

		if (request.getEmail() != null) {
			supplier.setEmail(request.getEmail());
		}

		if (request.getPhone() != null) {
			supplier.setPhone(request.getPhone());
		}

		if (request.getAddress() != null) {
			supplier.setAddress(request.getAddress());
		}

		if (request.getLeadTimeDays() != null) {
			supplier.setLeadTimeDays(request.getLeadTimeDays());
		}

		if (request.getIsActive() != null) {
			supplier.setActive(request.getIsActive());
		}

		Supplier updatedSupplier = supplierRepository.save(supplier);

		return toResponse(updatedSupplier);
	}

	// 6. Soft-delete supplier
	@Override
	public void deleteSupplier(Long id) {

		Supplier supplier = findExistingSupplier(id);

		supplier.setDeleted(true);
		supplier.setActive(false);

		supplierRepository.save(supplier);
	}

	// Find only suppliers that have not been deleted.
	private Supplier findExistingSupplier(Long id) {

		return supplierRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
	}

	// Convert entity to response DTO.
	private SupplierResponse toResponse(Supplier supplier) {

		return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getContactPerson(),
				supplier.getEmail(), supplier.getPhone(), supplier.getAddress(), supplier.getLeadTimeDays(),
				supplier.isActive(), supplier.isDeleted(), supplier.getCreatedAt(), supplier.getUpdatedOn(),
				supplier.getUpdatedBy());
	}
	
}
