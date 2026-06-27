package com.jsp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.entity.DeliveryAgent;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Integer> {
	Optional<DeliveryAgent> findByContact(String contact);

	Optional<DeliveryAgent> findByVehicleNumber(String vehicleNumber);

	List<DeliveryAgent> findByRatingGreaterThan(Double rating);

	boolean existsByContact(String contact);

	boolean existsByVehicleNumber(String vehicleNumber);
}
