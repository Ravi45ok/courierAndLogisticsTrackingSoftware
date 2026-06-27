package com.jsp.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.jsp.entity.Shipment;
import com.jsp.entity.ShipmentStatus;
import com.jsp.entity.TrackingHistory;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.repository.ShipmentRepository;
import com.jsp.repository.TrackingHistoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TrackingHistoryService {

	private TrackingHistoryRepository trackingHistoryRepository;
	private ShipmentRepository shipmentRepository;

	public List<TrackingHistory> getAllTrackingHistories() {
		List<TrackingHistory> trackingHistories = trackingHistoryRepository.findAll();
		if (trackingHistories.isEmpty())
			throw new ResourceNotFoundException("No tracking histories found");
		return trackingHistories;
	}

	public TrackingHistory getById(Integer id) {
		return trackingHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tracking history not found with id: " + id));
	}

	public List<TrackingHistory> getByTrackingNumber(String trackingNumber) {
		if (!shipmentRepository.existsByTrackingNumber(trackingNumber))
			throw new ResourceNotFoundException("No shipment found with tracking number: " + trackingNumber);
		List<TrackingHistory> list = trackingHistoryRepository.findByShipmentTrackingNumber(trackingNumber);
		if (list.isEmpty())
			throw new ResourceNotFoundException("No tracking history found for tracking number: " + trackingNumber);
		return list;
	}

	public List<TrackingHistory> getByStatus(ShipmentStatus status) {
		List<TrackingHistory> list = trackingHistoryRepository.findByStatus(status);
		if (list.isEmpty())
			throw new ResourceNotFoundException("No tracking history found with status: " + status);
		return list;
	}

	public List<TrackingHistory> getByShipment(Integer shipmentId) {
		if (!shipmentRepository.existsById(shipmentId))
			throw new ResourceNotFoundException("No shipment found with id: " + shipmentId);
		List<TrackingHistory> list = trackingHistoryRepository.findByShipmentId(shipmentId);
		if (list.isEmpty())
			throw new ResourceNotFoundException("No tracking history found for shipment id: " + shipmentId);
		return list;
	}

	public TrackingHistory updateTrackingHistory(Integer id, TrackingHistory updatedTracking) {
		TrackingHistory existing = trackingHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tracking history not found with id: " + id));

		existing.setLocation(updatedTracking.getLocation());
		existing.setRemarks(updatedTracking.getRemarks());
		existing.setStatus(updatedTracking.getStatus());

		Shipment shipment = shipmentRepository.findById(existing.getShipment().getId()).get();
		shipment.setStatus(updatedTracking.getStatus());

		return trackingHistoryRepository.save(existing);
	}
}