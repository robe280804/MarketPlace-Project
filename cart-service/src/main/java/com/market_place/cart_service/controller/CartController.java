package com.market_place.cart_service.controller;

import com.market_place.cart_service.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    //ADD, REMOVE, UPDATE QUANTITY

    public ResponseEntity<CartUpdateResponseDto> add(Long productId){
        return cartService.addProduct(productId);
    }


}
