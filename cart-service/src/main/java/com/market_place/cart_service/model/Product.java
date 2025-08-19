package com.market_place.cart_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//RemoveTime = dopo una settimana dell'inserimento del prodotto dal carrello, se non viene comprato verrà rimosso dal carrello
public class Product {

    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime removeTime;
}
