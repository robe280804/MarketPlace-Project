package com.market_place.cart_service.exception;

public class CartNotFoundEx extends RuntimeException {
    public CartNotFoundEx(String message) {
        super(message);
    }
}
