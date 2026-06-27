package com.jsp.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.entity.Payment;
import com.jsp.entity.PaymentStatus;
import com.jsp.exception.InvalidInputException;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.repository.PaymentRepository;

@Service
public class PaymentService {
	@Autowired
    private PaymentRepository paymentRepository;

	public List<Payment> fetchAll() {
		List<Payment> payments = paymentRepository.findAll();
		if (payments.isEmpty())
			throw new ResourceNotFoundException("No payments present in database");
		return payments;
	}

	public Payment fetchById(Integer id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No payment present with this id"));
		return payment;
	}

	public Payment updateStatus(Integer id, PaymentStatus newStatus) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No payment present with this id"));

		PaymentStatus currentStatus = payment.getPaymentStatus();
		validateNewStatus(currentStatus, newStatus);

		payment.setPaymentStatus(newStatus);
		return paymentRepository.save(payment);

	}
	public  Payment cancelPayment(Integer id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No payment present with this id"));

		PaymentStatus currentStatus = payment.getPaymentStatus();
		validateNewStatus(currentStatus, PaymentStatus.CANCELLED);

		payment.setPaymentStatus(PaymentStatus.CANCELLED);
		return paymentRepository.save(payment);
	}

	private void validateNewStatus(PaymentStatus currentStauts, PaymentStatus newStatus) {
		switch (currentStauts) {
		case PENDING:
			if (newStatus != PaymentStatus.SUCCESS && newStatus != PaymentStatus.FAILED
					&& newStatus != PaymentStatus.CANCELLED)
				throw new InvalidInputException("pending payment cannot move to Refund");
			break;
		case SUCCESS:
			if (newStatus != PaymentStatus.REFUNDED)
				throw new InvalidInputException("Success payment can only move to Refund");
			break;
		case FAILED:
			if (newStatus != PaymentStatus.SUCCESS && newStatus != PaymentStatus.CANCELLED)
				throw new InvalidInputException("Failed payment can only move towards seccuss or cancelled ");
			break;
		case CANCELLED:
			throw new InvalidInputException("payment has already been cancelled");
		case REFUNDED:
			throw new InvalidInputException("payment has alreday been refunded");
		default:
			throw new InvalidInputException("payment cannot move to this Status");

		}
	}

	

}
