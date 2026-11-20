
package com.becoder.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.becoder.dto.CustomerRequest;
import com.becoder.dto.CustomerResponse;
import com.becoder.entity.Customer;
import com.becoder.entity.Pharmacy;
import com.becoder.repository.CustomerRepository;
import com.becoder.repository.PharmacyRepository;
import com.becoder.service.CurrentUserService;
import com.becoder.service.CustomerService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PharmacyRepository pharmacyRepository;

	@Autowired
	private CurrentUserService currentUserService;

	// Create customer
	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElseThrow(() -> new EntityNotFoundException("Pharmacy not found"));

		Customer customer = new Customer();

		customer.setPharmacy(pharmacy);
		customer.setFullName(request.getFullName().trim());
		customer.setPhone(request.getPhone());
		customer.setEmail(request.getEmail());
		customer.setAddress(request.getAddress());

		Customer savedCustomer = customerRepository.save(customer);

		return mapToResponse(savedCustomer);
	}

	// Get all customers belonging to current pharmacy
	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> getAllCustomers() {
		Long pharmacyId = currentUserService.getCurrentPharmacyId();
		return customerRepository.findByPharmacy_IdAndIsDeletedFalse(pharmacyId).stream().map(this::mapToResponse).toList();
	}

	// Get customer by ID
	@Override
	@Transactional(readOnly = true)
	public CustomerResponse getCustomerById(Long id) {
		Long pharmacyId = currentUserService.getCurrentPharmacyId();
		Customer customer = getCustomerOrThrow(id, pharmacyId);
		return mapToResponse(customer);
	}

	// Search customers by name
	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> searchCustomersByName(String name) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		if (name == null || name.isBlank()) {
			return getAllCustomers();
		}

		return customerRepository
				.findByPharmacy_IdAndFullNameContainingIgnoreCaseAndIsDeletedFalse(pharmacyId, name.trim()).stream()
				.map(this::mapToResponse).toList();
	}

	// Update customer
	@Override
	public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

		Long pharmacyId = currentUserService.getCurrentPharmacyId();

		Customer customer = getCustomerOrThrow(id, pharmacyId);

		customer.setFullName(request.getFullName().trim());
		customer.setPhone(request.getPhone());
		customer.setEmail(request.getEmail());
		customer.setAddress(request.getAddress());

		Customer updatedCustomer = customerRepository.save(customer);

		return mapToResponse(updatedCustomer);
	}

	// Soft delete customer
	@Override
	public void deleteCustomer(Long id) {
		Long pharmacyId = currentUserService.getCurrentPharmacyId();
		Customer customer = getCustomerOrThrow(id, pharmacyId);
		customer.setDeleted(true);
		customerRepository.save(customer);
	}

	// Find customer within the authenticated pharmacy
	private Customer getCustomerOrThrow(Long id, Long pharmacyId) {
		return customerRepository.findByIdAndPharmacy_IdAndIsDeletedFalse(id, pharmacyId)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
	}

	// Convert entity to response DTO
	private CustomerResponse mapToResponse(Customer customer) {
		return new CustomerResponse(customer.getId(), customer.getFullName(), customer.getPhone(), customer.getEmail(),
				customer.getAddress(), customer.getCreatedOn(), customer.getUpdatedOn());
	}
}
