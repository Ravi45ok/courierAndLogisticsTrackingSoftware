package com.jsp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.entity.Shipment;
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>{
	Optional<Shipment> findByTrackingNumber(String trackingNumber);
    List<Shipment> findByCustomerId(Integer customerId);
    List<Shipment> findByWarehouseId(Integer warehouseId);
    List<Shipment> findByDeliveryAgentId(Integer deliveryAgentId);
    List<Shipment> findByDeliveryDate(LocalDate deliveryDate);
    List<Shipment> findBySourceAndDestination(String source, String destination);
	boolean existsByTrackingNumber(String trackingNumber);
    
}
