package com.market_place.cart_service.controller;

import com.market_place.cart_service.dto.CartUpdateResponseDto;
import com.market_place.cart_service.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    //ADD, REMOVE, UPDATE QUANTITY

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartUpdateResponseDto> add(@PathVariable Long productId){
        return ResponseEntity.ok(cartService.addProduct(productId));
    }


}
