package com.market_place.cart_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CartDto {

    private Long id;
    private UUID ownerId;
    private List<CartItemDto> cartItems;
}
