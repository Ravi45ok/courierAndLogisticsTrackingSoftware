package com.jsp.exception;

public class ShipmentAlreadyDeliveredException extends RuntimeException {
    public ShipmentAlreadyDeliveredException(String message) {
        super(message);
    }
}