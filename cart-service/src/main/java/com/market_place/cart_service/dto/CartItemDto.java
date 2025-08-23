package com.market_place.cart_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartItemDto {

    private UUID id;
    private Long productId;
    private String name;
    private Integer quantity;
    private Double price;
}
