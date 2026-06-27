package com.jsp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jsp.entity.DeliveryAgent;
import com.jsp.service.DeliveryAgentService;

@RestController
@RequestMapping("/deliveryAgent")
public class DeliveryAgentController {

	@Autowired
	private DeliveryAgentService deliveryAgentService;

	@PostMapping("/create")
	public ResponseEntity<DeliveryAgent> createAgent(@RequestBody DeliveryAgent agent) {
		return new ResponseEntity<>(deliveryAgentService.createAgent(agent), HttpStatus.CREATED);
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<List<DeliveryAgent>> getAllAgents() {
		return new ResponseEntity<>(deliveryAgentService.getAllAgents(), HttpStatus.OK);
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<DeliveryAgent> getById(@PathVariable int id) {
		return new ResponseEntity<>(deliveryAgentService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/fetch/vehicle/{vehicleNumber}")
	public ResponseEntity<DeliveryAgent> getByVehicleNumber(@PathVariable String vehicleNumber) {
		return new ResponseEntity<>(deliveryAgentService.getByVehicleNumber(vehicleNumber), HttpStatus.OK);
	}

	@GetMapping("/fetch/contact/{contact}")
	public ResponseEntity<DeliveryAgent> getByContact(@PathVariable String contact) {
		return new ResponseEntity<>(deliveryAgentService.getByContact(contact), HttpStatus.OK);
	}

	@GetMapping("/fetch/rating/{rating}")
	public ResponseEntity<List<DeliveryAgent>> getByRatingGreaterThan(@PathVariable Double rating) {
		return new ResponseEntity<>(deliveryAgentService.getByRatingGreaterThan(rating), HttpStatus.OK);
	}

	@GetMapping("/fetch/sort/rating")
	public ResponseEntity<List<DeliveryAgent>> sortByRatingDescending() {
		return new ResponseEntity<>(deliveryAgentService.sortByRatingDescending(), HttpStatus.OK);
	}

	@PutMapping("/update/{agentId}")
	public ResponseEntity<DeliveryAgent> updateAgent(@PathVariable Integer agentId, @RequestBody DeliveryAgent agent) {
		return new ResponseEntity<>(deliveryAgentService.updateAgent(agentId, agent), HttpStatus.OK);
	}

	@PutMapping("/update/availability/{agentId}/{status}")
	public ResponseEntity<DeliveryAgent> updateAgentAvailability(@PathVariable Integer agentId,
			@PathVariable Boolean status) {
		return new ResponseEntity<>(deliveryAgentService.updateAgentAvailability(agentId, status), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{agentId}")
	public ResponseEntity<String> deleteAgent(@PathVariable Integer agentId) {
		return new ResponseEntity<>(deliveryAgentService.deleteAgent(agentId), HttpStatus.OK);
	}
}