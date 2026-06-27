package com.jsp.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.entity.Shipment;
import com.jsp.entity.Warehouse;
import com.jsp.exception.InvalidInputException;
import com.jsp.exception.ResourceAlreadyExistsException;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.repository.WarehouseRepository;

@Service
public class WarehouseService {
	@Autowired
	private WarehouseRepository warehouseRepository;

	public @Nullable Warehouse createWarehouse(Warehouse warehouse) {
		if (warehouse.getContact().length() != 10)
			throw new InvalidInputException("Contact must be 10 digis");
		if (warehouseRepository.existsByContact(warehouse.getContact()))
			throw new ResourceAlreadyExistsException("Contact number must be unique");

		return warehouseRepository.save(warehouse);
	}

	public List<Warehouse> getAllWarehouses() {
		List<Warehouse> warehouses = warehouseRepository.findAll();
		if (warehouses.isEmpty())
			throw new ResourceNotFoundException("No warehouses found");
		return warehouses;
	}

	public Warehouse getById(int id) {

		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
		return warehouse;
	}

	public List<Warehouse> getByLocation(String location) {
		List<Warehouse> warehouses = warehouseRepository.findByLocation(location);
		if (warehouses.isEmpty())
			throw new ResourceNotFoundException("No warehouses found in location: " + location);
		return warehouses;
	}

	public List<Warehouse> getByCapacityGreaterThan(int capacity) {
		List<Warehouse> warehouses = warehouseRepository.findByCapacityGreaterThan(capacity);
		if (warehouses.isEmpty())
			throw new ResourceNotFoundException("No warehouses found with capacity greater than: " + capacity);
		return warehouses;
	}

	public Warehouse updateWarehouse(int id, Warehouse updatedWarehouse) {
		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

		if (updatedWarehouse.getContact().length() != 10)
			throw new InvalidInputException("Contact must be 10 digis");

		if (!warehouse.getContact().equals(updatedWarehouse.getContact())
				&& warehouseRepository.existsByContact(updatedWarehouse.getContact()))
			throw new ResourceAlreadyExistsException(
					"Warehouse already exists with contact: " + updatedWarehouse.getContact());

		warehouse.setName(updatedWarehouse.getName());
		warehouse.setLocation(updatedWarehouse.getLocation());
		warehouse.setCapacity(updatedWarehouse.getCapacity());
		warehouse.setContact(updatedWarehouse.getContact());

		return warehouseRepository.save(warehouse);
	}

	public String deleteWarehouse(int id) {
		Warehouse warehouse = warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No warehouse present with this id"));
		List<Shipment> shipments = warehouse.getShipment();
		if (!shipments.isEmpty())
			throw new InvalidInputException("Warehouse has some existing shipments");
		warehouseRepository.deleteById(id);
		return "Warehouse deleted seccussfully";
	}

}
