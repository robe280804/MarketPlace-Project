package com.market_place.product_service.mapper;

import com.market_place.product_service.dto.ProductResponseDto;
import com.market_place.product_service.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDto fromProductToDto(Product savedProduct){
        return ProductResponseDto.builder()
                .id(savedProduct.getId())
                .userId(savedProduct.getCreatorId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .image(savedProduct.getImage())
                .price(savedProduct.getPrice())
                .quantity(savedProduct.getQuantity())
                .category(savedProduct.getCategory())
                .createdAt(savedProduct.getCreatedAt())
                .build();
    }
}
