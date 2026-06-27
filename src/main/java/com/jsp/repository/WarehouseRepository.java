package com.jsp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.entity.Warehouse;
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

	boolean existsByContact(String contact);
	List<Warehouse> findByLocation(String location);
    List<Warehouse> findByCapacityGreaterThan(int capacity);

}
