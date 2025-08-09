package com.market_place.auth_service;

public class UserAlredyRegisterEx extends RuntimeException {
    public UserAlredyRegisterEx(String message) {
        super(message);
    }
}
