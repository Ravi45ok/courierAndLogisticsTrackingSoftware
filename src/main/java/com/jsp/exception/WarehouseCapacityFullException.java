package com.jsp.exception;

public class WarehouseCapacityFullException extends RuntimeException {
    public WarehouseCapacityFullException(String message) {
        super(message);
    }
}