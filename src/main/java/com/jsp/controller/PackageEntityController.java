package com.jsp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.entity.PackageEntity;
import com.jsp.service.PackageEntityService;

@RestController
@RequestMapping("/package")

public class PackageEntityController {
    @Autowired
    private PackageEntityService packageEntityService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<PackageEntity>> getAllPackages() {
        return new ResponseEntity<>(packageEntityService.getAllPackages(), HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<PackageEntity> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(packageEntityService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/fetch/shipment/{shipmentId}")
    public ResponseEntity<PackageEntity> getByShipment(@PathVariable Integer shipmentId) {
        return new ResponseEntity<>(packageEntityService.getByShipment(shipmentId), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PackageEntity> updatePackage(
        @PathVariable Integer id,
        @RequestBody PackageEntity packageEntity) {
        return new ResponseEntity<>(packageEntityService.updatePackage(id, packageEntity), HttpStatus.OK);
    }
}
