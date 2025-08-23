package com.market_place.cart_service.service;

import com.market_place.cart_service.dto.CartDto;
import com.market_place.cart_service.dto.CartUpdateResponseDto;
import com.market_place.cart_service.dto.PurchaseRequestDto;

import java.util.UUID;

public interface CartService {
    CartUpdateResponseDto addProduct(Long productId, PurchaseRequestDto request);

    CartUpdateResponseDto remove(Long productId);

    CartDto getUserCart();
}
