package com.market_place.cart_service.service;

import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<CartUpdateResponseDto> addProduct(Long productId);
}
