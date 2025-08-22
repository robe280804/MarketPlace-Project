package com.market_place.cart_service.service;

import com.market_place.cart_service.dto.CartUpdateResponseDto;

public interface CartService {
    CartUpdateResponseDto addProduct(Long productId);
}
