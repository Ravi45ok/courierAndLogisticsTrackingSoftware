package com.jsp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.entity.PackageEntity;

public interface PackageEntityRepository extends JpaRepository<PackageEntity, Integer> {
    Optional<PackageEntity> findByShipmentId(Integer shipmentId);
}