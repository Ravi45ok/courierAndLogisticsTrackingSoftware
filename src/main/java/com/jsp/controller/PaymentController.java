package com.jsp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.entity.Payment;
import com.jsp.entity.PaymentStatus;
import com.jsp.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/fetchAll")
	public ResponseEntity<List<Payment>> fetchAll() {
		return new ResponseEntity<>(paymentService.fetchAll(), HttpStatus.OK);
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<Payment> fetchById(@PathVariable Integer id) {
		return new ResponseEntity<>(paymentService.fetchById(id), HttpStatus.OK);
	}
	
	@PutMapping("/update/status/{id}/{status}")
	public ResponseEntity<Payment> updateStatus(@PathVariable Integer id, @PathVariable PaymentStatus status){
		return new ResponseEntity<>(paymentService.updateStatus(id, status), HttpStatus.OK);
	}
	
	@PutMapping("/cancelPayment/{id}")
	public ResponseEntity<Payment> cancelPayment(@PathVariable Integer id){
		return new ResponseEntity<>(paymentService.cancelPayment(id),HttpStatus.OK);
	}
}
