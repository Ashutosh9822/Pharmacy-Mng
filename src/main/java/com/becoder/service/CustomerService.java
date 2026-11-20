
package com.becoder.service;

import java.util.List;

import com.becoder.dto.CustomerRequest;
import com.becoder.dto.CustomerResponse;

public interface CustomerService {

	// Create a new customer
	CustomerResponse createCustomer(CustomerRequest request);

	// Get all customers belonging to the current pharmacy
	List<CustomerResponse> getAllCustomers();

	// Get customer by ID
	CustomerResponse getCustomerById(Long id);

	// Search customers by name
	List<CustomerResponse> searchCustomersByName(String name);

	// Update customer details
	CustomerResponse updateCustomer(Long id, CustomerRequest request);

	// Soft delete customer
	void deleteCustomer(Long id);
}
