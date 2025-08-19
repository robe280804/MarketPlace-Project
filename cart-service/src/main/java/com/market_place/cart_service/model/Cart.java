package com.market_place.cart_service.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Cart {

    private Long id;
    private UUID userOwner;
    private List<Product> cartItem;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
