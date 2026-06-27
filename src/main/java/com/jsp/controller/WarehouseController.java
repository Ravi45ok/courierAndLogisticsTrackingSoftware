package com.jsp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.entity.Warehouse;
import com.jsp.service.WarehouseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
	@Autowired
	private WarehouseService warehouseService;

	@PostMapping("/create")
	public ResponseEntity<Warehouse> createWarehouse( @RequestBody Warehouse warehouse) {
		return new ResponseEntity<>(warehouseService.createWarehouse(warehouse), HttpStatus.CREATED);
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<List<Warehouse>> getAllWarehouses() {
		return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<Warehouse> getById(@PathVariable int id) {
		return new ResponseEntity<>(warehouseService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/fetch/location/{location}")
	public ResponseEntity<List<Warehouse>> getByLocation(@PathVariable String location) {
		return new ResponseEntity<>(warehouseService.getByLocation(location), HttpStatus.OK);
	}

	@GetMapping("/fetch/capacity/{capacity}")
	public ResponseEntity<List<Warehouse>> getByCapacityGreaterThan(@PathVariable int capacity) {
		return new ResponseEntity<>(warehouseService.getByCapacityGreaterThan(capacity), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Warehouse> updateWarehouse(@PathVariable int id, @RequestBody Warehouse warehouse) {
		return new ResponseEntity<>(warehouseService.updateWarehouse(id, warehouse), HttpStatus.OK);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteWarehouse(@PathVariable int id){
		return new ResponseEntity<>(warehouseService.deleteWarehouse(id),HttpStatus.OK);
	}
}
