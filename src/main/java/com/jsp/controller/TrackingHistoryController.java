package com.jsp.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jsp.entity.ShipmentStatus;
import com.jsp.entity.TrackingHistory;
import com.jsp.service.TrackingHistoryService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tracking-history")
@AllArgsConstructor
public class TrackingHistoryController {

    private TrackingHistoryService trackingHistoryService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<TrackingHistory>> getAllTrackingHistories() {
        return new ResponseEntity<>(trackingHistoryService.getAllTrackingHistories(), HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<TrackingHistory> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(trackingHistoryService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/fetch/tracking/{trackingNumber}")
    public ResponseEntity<List<TrackingHistory>> getByTrackingNumber(@PathVariable String trackingNumber) {
        return new ResponseEntity<>(trackingHistoryService.getByTrackingNumber(trackingNumber), HttpStatus.OK);
    }

    @GetMapping("/fetch/status/{status}")
    public ResponseEntity<List<TrackingHistory>> getByStatus(@PathVariable ShipmentStatus status) {
        return new ResponseEntity<>(trackingHistoryService.getByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/fetch/shipment/{shipmentId}")
    public ResponseEntity<List<TrackingHistory>> getByShipment(@PathVariable Integer shipmentId) {
        return new ResponseEntity<>(trackingHistoryService.getByShipment(shipmentId), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TrackingHistory> updateTrackingHistory(
        @PathVariable Integer id,
        @RequestBody TrackingHistory trackingHistory) {
        return new ResponseEntity<>(trackingHistoryService.updateTrackingHistory(id, trackingHistory), HttpStatus.OK);
    }
}
