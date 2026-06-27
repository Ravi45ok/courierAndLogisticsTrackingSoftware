package com.jsp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsp.dto.ShipmentUpdateDto;
import com.jsp.entity.Customer;
import com.jsp.entity.DeliveryAgent;
import com.jsp.entity.Payment;
import com.jsp.entity.PaymentStatus;
import com.jsp.entity.Shipment;
import com.jsp.entity.ShipmentStatus;
import com.jsp.entity.TrackingHistory;
import com.jsp.entity.Warehouse;
import com.jsp.exception.AgentNotAvailableException;
import com.jsp.exception.InvalidInputException;
import com.jsp.exception.InvalidShipmentStatusException;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.exception.WarehouseCapacityFullException;
import com.jsp.repository.CustomerRepository;
import com.jsp.repository.DeliveryAgentRepository;
import com.jsp.repository.ShipmentRepository;
import com.jsp.repository.TrackingHistoryRepository;
import com.jsp.repository.WarehouseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShipmentService {

	private ShipmentRepository shipmentRepository;
	private CustomerRepository customerRepository;
	private WarehouseRepository warehouseRepository;
	private DeliveryAgentRepository deliveryAgentRepository;
	private TrackingHistoryRepository trackingHistoryRepository;
    
	@Transactional
	public Shipment createShipment(Shipment shipment) {

		Customer customer = customerRepository.findById(shipment.getCustomer().getId())
				.orElseThrow(() -> new ResourceNotFoundException("No Customer exist with this id"));
		shipment.setCustomer(customer);

		Warehouse warehouse = warehouseRepository.findById(shipment.getWarehouse().getId())
				.orElseThrow(() -> new ResourceNotFoundException("No Warehouse exist with this id"));
		if (warehouse.getCapacity() <= 0)
			throw new WarehouseCapacityFullException("Warehouse is full");
		warehouse.setCapacity(warehouse.getCapacity() - 1);
		warehouseRepository.save(warehouse);
		shipment.setWarehouse(warehouse);

		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(shipment.getDeliveryAgent().getId())
				.orElseThrow(() -> new ResourceNotFoundException("No DeleveryAgent exist with this id"));
		if (!deliveryAgent.getAvailabilityStatus())
			throw new AgentNotAvailableException("Agent is currently not available");
		deliveryAgent.setAvailabilityStatus(false);
		deliveryAgentRepository.save(deliveryAgent);
		shipment.setDeliveryAgent(deliveryAgent);

		double amount = calculateAmount(shipment);
		Payment payment = shipment.getPayment();
		payment.setAmount(amount);
		payment.setPaymentStatus(PaymentStatus.PENDING);
		shipment.setPayment(payment);

		String trackingNumber = createTrackingNumber();
		shipment.setTrackingNumber(trackingNumber);

		shipment.setStatus(ShipmentStatus.PENDING);

		Shipment savedShipment = shipmentRepository.save(shipment);

		TrackingHistory trackingHistory = new TrackingHistory();
		
		
		trackingHistory.setLocation(savedShipment.getSource());
		trackingHistory.setRemarks("Shipment has been Created");
		trackingHistory.setStatus(savedShipment.getStatus());
		trackingHistory.setShipment(savedShipment);
		trackingHistoryRepository.save(trackingHistory);
        
		List<TrackingHistory> trackingHistories= List.of(trackingHistory);// will not hit the database just to syncronized the response properly
		savedShipment.setTrackingHistory(trackingHistories);
//		if(10==10)
//			throw new NullPointerException();
		return savedShipment;
	}

	public List<Shipment> fetchAll() {
		List<Shipment> shipments = shipmentRepository.findAll();
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipment present");
		return shipments;
	}

	public Shipment fetchById(int id) {
		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No shipment Available with this id : "+ id));
		return shipment;
	}

	public Shipment getByTrackingNumber(String trackingNumber) {
		return shipmentRepository.findByTrackingNumber(trackingNumber).orElseThrow(
				() -> new ResourceNotFoundException("Shipment not found with tracking number: " + trackingNumber));
	}

	public List<Shipment> getShipmentsOfACustomer(Integer customerId) {
		if (!customerRepository.existsById(customerId))
			throw new ResourceNotFoundException("Customer not found with id: " + customerId);
		List<Shipment> shipments = shipmentRepository.findByCustomerId(customerId);
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipments found for customer id: " + customerId);
		return shipments;
	}

	public List<Shipment> getShipmentsInAWarehouse(Integer warehouseId) {
		if (!warehouseRepository.existsById(warehouseId))
			throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
		List<Shipment> shipments = shipmentRepository.findByWarehouseId(warehouseId);
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipments found in warehouse id: " + warehouseId);
		return shipments;
	}

	public List<Shipment> getShipmentsAssignedToAgent(Integer agentId) {
		if (!deliveryAgentRepository.existsById(agentId))
			throw new ResourceNotFoundException("Agent not found with id: " + agentId);
		List<Shipment> shipments = shipmentRepository.findByDeliveryAgentId(agentId);
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipments found for agent id: " + agentId);
		return shipments;
	}

	public List<Shipment> getByDeliveryDate(LocalDate deliveryDate) {
		List<Shipment> shipments = shipmentRepository.findByDeliveryDate(deliveryDate);
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipments found for delivery date: " + deliveryDate);
		return shipments;
	}

	public List<Shipment> getBySourceAndDestination(String source, String destination) {
		List<Shipment> shipments = shipmentRepository.findBySourceAndDestination(source, destination);
		if (shipments.isEmpty())
			throw new ResourceNotFoundException("No shipments found from " + source + " to " + destination);
		return shipments;
	}

	public Page<Shipment> getBySortedPages(Integer pageNumber, Integer pageSize) {
		Page<Shipment> page = shipmentRepository
				.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()));
		if (page.isEmpty())
			throw new ResourceNotFoundException("No Shipment present with this pageNumber and pageSize");
		return page;
	}
    
	@Transactional
	public Shipment updateStatus(ShipmentUpdateDto shipmentUpdateDto) {
		Integer id = shipmentUpdateDto.getShipmentId();
		String location = shipmentUpdateDto.getLocation();
		String remarks = shipmentUpdateDto.getRemarks();
		ShipmentStatus newStatus = shipmentUpdateDto.getStatus();

		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No shipment Exist with this id"));

		validateStatusTransition(shipment.getStatus(), newStatus);
		shipment.setStatus(newStatus);
		shipmentRepository.save(shipment);

		if (newStatus == ShipmentStatus.DELIVERED || newStatus == ShipmentStatus.CANCELLED
				|| newStatus == ShipmentStatus.RETURNED) {
			DeliveryAgent agent = shipment.getDeliveryAgent();
			agent.setAvailabilityStatus(true);
			deliveryAgentRepository.save(agent);

			Warehouse warehouse = shipment.getWarehouse();
			warehouse.setCapacity(warehouse.getCapacity() + 1);
			warehouseRepository.save(warehouse);
		}

		TrackingHistory trackingHistory = new TrackingHistory();
		trackingHistory.setLocation(location);
		trackingHistory.setStatus(newStatus);
		trackingHistory.setRemarks(remarks);
		trackingHistory.setShipment(shipment);
		trackingHistoryRepository.save(trackingHistory);

		return shipmentRepository.findById(id).get();
	}

	public Shipment assignDeliveryAgent(Integer shipmentId, Integer deliveryAgentId) {
		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ResourceNotFoundException("No shipment exist with this id"));

		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(deliveryAgentId)
				.orElseThrow(() -> new ResourceNotFoundException("No deliveryAgent with this Id"));

		if (shipment.getDeliveryAgent().getId().equals(deliveryAgentId))
			throw new InvalidInputException("This Delivery Agent is already Assign to this Shipment ");

		if (!deliveryAgent.getAvailabilityStatus())
			throw new AgentNotAvailableException("Delivery Agent is not available");

		DeliveryAgent oldAgent = shipment.getDeliveryAgent();
		oldAgent.setAvailabilityStatus(true);
		deliveryAgentRepository.save(oldAgent);
		shipment.setDeliveryAgent(deliveryAgent);

		deliveryAgent.setAvailabilityStatus(false);
		deliveryAgentRepository.save(deliveryAgent);
		return shipmentRepository.save(shipment);
	}

	public Shipment assignWarehouse(Integer shipmentId, Integer warehouseId) {
		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ResourceNotFoundException("No shipment exist with this id"));

		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException("No warehouse exist with this id"));

		if (shipment.getWarehouse().getId().equals(warehouseId))
			throw new InvalidInputException("This Warehouse is already assigned to this Shipment");

		if (warehouse.getCapacity() <= 0)
			throw new WarehouseCapacityFullException("Warehouse is full");

		Warehouse oldWarehouse = shipment.getWarehouse();
		oldWarehouse.setCapacity(oldWarehouse.getCapacity() + 1);
		warehouseRepository.save(oldWarehouse);

		warehouse.setCapacity(warehouse.getCapacity() - 1);
		warehouseRepository.save(warehouse);

		shipment.setWarehouse(warehouse);
		return shipmentRepository.save(shipment);
	}

	public String deleteShipment(Integer shipmentId) {
		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ResourceNotFoundException("No shipment exist with this id"));

		DeliveryAgent deliveryAgent = shipment.getDeliveryAgent();
		deliveryAgent.setAvailabilityStatus(true);
		deliveryAgentRepository.save(deliveryAgent);

		Warehouse warehouse = shipment.getWarehouse();
		warehouse.setCapacity(warehouse.getCapacity() + 1);
		warehouseRepository.save(warehouse);

		shipmentRepository.deleteById(shipmentId);
		return "Shipment deleted successfully";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	private String createTrackingNumber() {
	    Random random = new Random();
	    return "TRKN" + (100000000 + random.nextInt(900000000));
	}
	// output: TRK123456789

	private double calculateAmount(Shipment shipment) {
		int distance = Math
				.abs((shipment.getSource().hashCode() % 10) * 100 - (shipment.getDestination().hashCode() % 10) * 100);
		double amount = distance * 10 + shipment.getWeight() * 5;
		if (shipment.getPackageEntity().getFragile())
			amount = amount + amount * .20;
		return amount;
	}

	private void validateStatusTransition(ShipmentStatus CurrentStatus, ShipmentStatus newStatus) {

		switch (CurrentStatus) {
		case PENDING:
			if (newStatus != ShipmentStatus.PICKED_UP && newStatus != ShipmentStatus.CANCELLED)
				throw new InvalidShipmentStatusException("Shipment can only move to pickup or cancel form pending");
			break;
		case PICKED_UP:
			if (newStatus != ShipmentStatus.IN_TRANSIT)
				throw new InvalidShipmentStatusException("Shipment can only move to intransit form pick up");
			break;
		case IN_TRANSIT:
			if (newStatus != ShipmentStatus.AT_WAREHOUSE)
				throw new InvalidShipmentStatusException("Shipment can only move to Warehouse  form In_Transit");
			break;
		case AT_WAREHOUSE:
			if (newStatus != ShipmentStatus.OUT_FOR_DELIVERY)
				throw new InvalidShipmentStatusException("Shipment can only move to out for delivery form warehouse");
			break;
		case OUT_FOR_DELIVERY:
			if (newStatus != ShipmentStatus.DELIVERED && newStatus != ShipmentStatus.FAILED_DELIVERY)
				throw new InvalidShipmentStatusException(
						"Shipment can only move to Delivered or failed delivery form out for delivery");
			break;
		case FAILED_DELIVERY:
			if (newStatus != ShipmentStatus.RETURNED)
				throw new InvalidShipmentStatusException("Shipment can only move to Returned  form Failed Delivery");
			break;
		case DELIVERED:
			throw new InvalidShipmentStatusException("Shipment is alrady Delivered");
		case RETURNED:
			throw new InvalidShipmentStatusException("Shipment is alrady Returned");
		case CANCELLED:
			throw new InvalidShipmentStatusException("Shipment is alrady Cancelled");
		default:
			throw new InvalidShipmentStatusException("Invalid status transition");

		}
	}

}
