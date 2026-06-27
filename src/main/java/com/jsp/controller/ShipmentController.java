package com.jsp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.jsp.dto.ShipmentUpdateDto;
import com.jsp.entity.Shipment;
import com.jsp.service.ShipmentService;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
     
	@Autowired
	private ShipmentService shipmentService;
	
	@PostMapping("/create")
	public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment){
		return new ResponseEntity<>(shipmentService.createShipment(shipment), HttpStatus.CREATED);
	}
	
	@GetMapping("/fetchAll")
	public ResponseEntity<List<Shipment>> fetchAll(){
		return new ResponseEntity<>(shipmentService.fetchAll(), HttpStatus.OK);
	}
	
	@GetMapping("/fetch/{id}")
	public ResponseEntity<Shipment> fetchById(@PathVariable int id){
		return new ResponseEntity<>(shipmentService.fetchById(id),HttpStatus.OK);
	}
	
	@GetMapping("/fetch/tracking/{trackingNumber}")
	public ResponseEntity<Shipment> getByTrackingNumber(@PathVariable String trackingNumber) {
	    return new ResponseEntity<>(shipmentService.getByTrackingNumber(trackingNumber), HttpStatus.OK);
	}

	@GetMapping("/fetch/customer/{customerId}")
	public ResponseEntity<List<Shipment>> getShipmentsOfACustomer(@PathVariable Integer customerId) {
	    return new ResponseEntity<>(shipmentService.getShipmentsOfACustomer(customerId), HttpStatus.OK);
	}

	@GetMapping("/fetch/warehouse/{warehouseId}")
	public ResponseEntity<List<Shipment>> getShipmentsInAWarehouse(@PathVariable Integer warehouseId) {
	    return new ResponseEntity<>(shipmentService.getShipmentsInAWarehouse(warehouseId), HttpStatus.OK);
	}

	@GetMapping("/fetch/agent/{agentId}")
	public ResponseEntity<List<Shipment>> getShipmentsAssignedToAgent(@PathVariable Integer agentId) {
	    return new ResponseEntity<>(shipmentService.getShipmentsAssignedToAgent(agentId), HttpStatus.OK);
	}

	@GetMapping("/fetch/deliveryDate/{deliveryDate}")
	public ResponseEntity<List<Shipment>> getByDeliveryDate(@PathVariable LocalDate deliveryDate) {
	    return new ResponseEntity<>(shipmentService.getByDeliveryDate(deliveryDate), HttpStatus.OK);
	}

	@GetMapping("/fetch/source/{source}/destination/{destination}")
	public ResponseEntity<List<Shipment>> getBySourceAndDestination(
	    @PathVariable String source,
	    @PathVariable String destination) {
	    return new ResponseEntity<>(shipmentService.getBySourceAndDestination(source, destination), HttpStatus.OK);
	}
	@GetMapping("/fetch/sortedPages/{pageNumber}/{pageSize}")
	public ResponseEntity<Page<Shipment>> getBySortedPages(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
		return new ResponseEntity<>(shipmentService.getBySortedPages(pageNumber, pageSize), HttpStatus.OK);
	}
	@PutMapping("/update/status")
	public ResponseEntity<Shipment> updateStatus( @RequestBody ShipmentUpdateDto shipmentUpdateDto){
		return new ResponseEntity<>(shipmentService.updateStatus(shipmentUpdateDto), HttpStatus.OK); 
	}
	@PutMapping("assign/deliveryAgent/{shipmentId}/{deliveryAgentId}")
	public ResponseEntity<Shipment> assignDeliveryAgent(@PathVariable Integer shipmentId, @PathVariable Integer deliveryAgentId){
		return new ResponseEntity<> (shipmentService.assignDeliveryAgent(shipmentId,deliveryAgentId),HttpStatus.OK);
	}
	@PutMapping("assign/deliveryAgent/{shipmentId}/{warehouseId}")
	public ResponseEntity<Shipment> assignWarehouse(@PathVariable Integer shipmentId, @PathVariable Integer warehouseId){
		return new ResponseEntity<> (shipmentService.assignWarehouse(shipmentId,warehouseId),HttpStatus.OK);
	}
	@DeleteMapping("/delete/{shipmentId}")
	public ResponseEntity<String> deleteShipment(@PathVariable Integer shipmentId){
		return new ResponseEntity<>(shipmentService.deleteShipment(shipmentId),HttpStatus.OK);
	}
	
	
}
