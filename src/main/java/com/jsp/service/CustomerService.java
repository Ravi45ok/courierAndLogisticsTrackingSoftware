package com.jsp.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jsp.entity.Customer;
import com.jsp.entity.Shipment;
import com.jsp.exception.InvalidInputException;
import com.jsp.exception.ResourceAlreadyExistsException;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	public Customer createCustomer(Customer customer) {
		if (customerRepository.existsByEmail(customer.getEmail()))
			throw new ResourceAlreadyExistsException("Email is already used");

		if (customer.getContact().length() != 10)
			throw new InvalidInputException("Contact number must be 10 digits");

		if (customerRepository.existsByContact(customer.getContact()))
			throw new ResourceAlreadyExistsException("Contact number is already used");
		return customerRepository.save(customer);
	}

	public List<Customer> fetchAll() {
		List<Customer> customers = customerRepository.findAll();
		if (customers.isEmpty())
			throw new ResourceNotFoundException("No Customer present in database");
		return customers;
	}

	public Customer fetchById(int id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Customer present in database with this id"));
		return customer;

	}

	public Customer getByEmail(String email) {
		Customer customer = customerRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
		return customer;
	}

	public Customer getByContact(String contact) {
		if (contact.length() != 10)
			throw new InvalidInputException("Contact must be 10 digits");
		Customer customer = customerRepository.findByContact(contact)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with contact: " + contact));
		return customer;
		
	}

	public Customer updateCustomer(int id, Customer updatedCustomer) {
		Customer existing = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Customer present in database with this id"));
		
		if (!existing.getEmail().equals(updatedCustomer.getEmail())
				&& customerRepository.existsByEmail(updatedCustomer.getEmail()))
			throw new ResourceAlreadyExistsException("Email already used: " + updatedCustomer.getEmail());

		if (!existing.getContact().equals(updatedCustomer.getContact())
				&& customerRepository.existsByContact(updatedCustomer.getContact()))
			throw new ResourceAlreadyExistsException("Contact already used: " + updatedCustomer.getContact());

		if (updatedCustomer.getContact().length() != 10)
			throw new InvalidInputException("Contact must be 10 digits");

		existing.setName(updatedCustomer.getName());
		existing.setEmail(updatedCustomer.getEmail());
		existing.setContact(updatedCustomer.getContact());
		existing.setAddress(updatedCustomer.getAddress());

		return customerRepository.save(existing);
	}

	public Page<Customer> fetchByPagesAndSortd(int pageSize, int pageNumber, String field, String order) {
		Page<Customer> page = null;
		if (order.equalsIgnoreCase("asc"))
			page = customerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
		else
			page = customerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).descending()));
		return page;
	}

	public String deleteCustomer(Integer id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No customer Exist with this id"));
		List<Shipment> shipments = customer.getShipment();
		if (!shipments.isEmpty())
			throw new InvalidInputException("Customer has active shipments");
		customerRepository.deleteById(id);
		return "Customer has been deleted";
	}

}
