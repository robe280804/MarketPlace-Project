package com.market_place.cart_service.controller;

import com.market_place.cart_service.dto.CartDto;
import com.market_place.cart_service.dto.CartUpdateResponseDto;
import com.market_place.cart_service.dto.PurchaseRequestDto;
import com.market_place.cart_service.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    //ADD, REMOVE, UPDATE QUANTITY

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartUpdateResponseDto> add(@PathVariable Long productId, @RequestBody PurchaseRequestDto request){
        return ResponseEntity.ok(cartService.addProduct(productId, request));
    }

    //Rimuove il prodotto dal carrello, e aggiunge la sua quantità nel microservizio product-service
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartUpdateResponseDto> remove(@PathVariable Long productId){
        return ResponseEntity.ok(cartService.remove(productId));
    }

    @GetMapping("/user")
    public ResponseEntity<CartDto> getUserCart(){
        return ResponseEntity.ok(cartService.getUserCart());
    }
}
