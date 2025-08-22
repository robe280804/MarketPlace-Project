package com.market_place.cart_service.controller;

import com.market_place.cart_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/api/product/{productId}")
    ProductResponseDto getProduct(@PathVariable Long productId);
}
