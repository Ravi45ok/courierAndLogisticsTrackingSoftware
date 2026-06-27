package com.jsp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	boolean existsByEmail(String email);

	boolean existsByContact(String contact);
	
	Optional<Customer> findByEmail(String email);
	Optional<Customer> findByContact(String contact);
	

}
