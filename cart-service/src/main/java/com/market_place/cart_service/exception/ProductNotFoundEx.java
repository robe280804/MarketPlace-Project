package com.market_place.cart_service.exception;

public class ProductNotFoundEx extends RuntimeException {
    public ProductNotFoundEx(String message) {
        super(message);
    }
}
