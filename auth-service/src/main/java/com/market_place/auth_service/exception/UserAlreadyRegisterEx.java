package com.market_place.auth_service.exception;

public class UserAlreadyRegisterEx extends RuntimeException {
    public UserAlreadyRegisterEx(String message) {
        super(message);
    }
}
