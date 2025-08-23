package com.market_place.cart_service.controller;

import com.market_place.cart_service.dto.ProductResponseDto;
import com.market_place.cart_service.dto.PurchaseRequestDto;
import com.market_place.cart_service.dto.QuantityUpdateDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/api/product/{productId}/purchase")
    ProductResponseDto purchaseProduct(@PathVariable Long productId, @RequestBody PurchaseRequestDto request);

    @PutMapping("/api/product/")
    ProductResponseDto updateQuantity(@Valid @RequestBody QuantityUpdateDto request);
}
