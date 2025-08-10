package com.market_place.product_service.service;

import com.market_place.product_service.dto.ProductRequestDto;
import com.market_place.product_service.dto.ProductResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(@Valid ProductRequestDto request);

    List<ProductResponseDto> getProducts();
}
