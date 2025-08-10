package com.market_place.auth_service.exception;

public class UserAlredyRegisterEx extends RuntimeException {
    public UserAlredyRegisterEx(String message) {
        super(message);
    }
}
