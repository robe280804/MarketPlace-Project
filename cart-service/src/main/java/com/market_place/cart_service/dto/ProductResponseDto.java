package com.market_place.cart_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductResponseDto {

    private Long id;
    private UUID userId;
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private Category category;
    private LocalDateTime createdAt;
}
