package com.market_place.auth_service.exception;

public class MissingAuthHeaderEx extends RuntimeException {
    public MissingAuthHeaderEx(String message) {
        super(message);
    }
}
