package com.jsp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.jsp.entity.PackageEntity;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.repository.PackageEntityRepository;
import com.jsp.repository.ShipmentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PackageEntityService {

    private PackageEntityRepository packageEntityRepository;
    private ShipmentRepository shipmentRepository;

    public List<PackageEntity> getAllPackages() {
        List<PackageEntity> packages = packageEntityRepository.findAll();
        if(packages.isEmpty())
            throw new ResourceNotFoundException("No packages found");
        return packages;
    }

    public PackageEntity getById(Integer id) {
        return packageEntityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
    }

    public PackageEntity getByShipment(Integer shipmentId) {
        if(!shipmentRepository.existsById(shipmentId))
            throw new ResourceNotFoundException("No shipment found with id: " + shipmentId);
        return packageEntityRepository.findByShipmentId(shipmentId)
            .orElseThrow(() -> new ResourceNotFoundException("No package found for shipment id: " + shipmentId));
    }

    public PackageEntity updatePackage(Integer id, PackageEntity updatedPackage) {
        PackageEntity existing = packageEntityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        existing.setPackageType(updatedPackage.getPackageType());
        existing.setFragile(updatedPackage.getFragile());
        existing.setDimension(updatedPackage.getDimension());

        return packageEntityRepository.save(existing);
    }
}
