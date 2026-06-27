package com.jsp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.entity.Customer;
import com.jsp.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	private CustomerService customerService;
	public CustomerController(CustomerService customerService) {
		this.customerService=customerService;
	}
	
	@PostMapping("/create")
     public ResponseEntity<Customer> createCustomer( @Valid  @RequestBody Customer customer) {
    	 return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
     }
	
	@GetMapping("/fetchAll")
	public ResponseEntity<List<Customer>> fetchAllCustomer(){
		return new ResponseEntity<>(customerService.fetchAll(), HttpStatus.OK);
	}
	
	@GetMapping("/fetch/id/{id}")
	public ResponseEntity<Customer> fetchById(@PathVariable int id){
		return new ResponseEntity<>(customerService.fetchById(id),HttpStatus.OK);
	}
	
	@GetMapping("/fetch/email/{email}")
	public ResponseEntity<Customer> getByEmail(@PathVariable String email) {
	    return new ResponseEntity<>(customerService.getByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/fetch/contact/{contact}")
	public ResponseEntity<Customer> getByContact(@PathVariable String contact) {
	    return new ResponseEntity<>(customerService.getByContact(contact), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
	    return new ResponseEntity<>(customerService.updateCustomer(id, customer), HttpStatus.OK);
	}
	
	@GetMapping("/fetch/{pageSize}/{pageNumber}/{field}/{order}")
	public ResponseEntity<Page<Customer>> fetchByPagesAndSortd(@PathVariable int pageSize, @PathVariable int pageNumber, 
			@PathVariable String field, @PathVariable String order ){
		return new ResponseEntity<>(customerService.fetchByPagesAndSortd(pageSize,pageNumber,field,order),HttpStatus.OK);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer id){
		return new ResponseEntity<>(customerService.deleteCustomer(id),HttpStatus.OK);
	}
}
