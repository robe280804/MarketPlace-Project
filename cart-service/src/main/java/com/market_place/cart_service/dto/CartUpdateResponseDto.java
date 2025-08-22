package com.market_place.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CartUpdateResponseDto {

    private String message;
    private UUID userId;
    private Long cartId;
    private Long productId;
    private Integer quantity;
}
