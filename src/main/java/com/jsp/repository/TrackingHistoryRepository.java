package com.jsp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.entity.TrackingHistory;
import com.jsp.entity.ShipmentStatus;

public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Integer> {
    List<TrackingHistory> findByStatus(ShipmentStatus status);
    List<TrackingHistory> findByShipmentId(Integer shipmentId);
    List<TrackingHistory> findByShipmentTrackingNumber(String trackingNumber);
}