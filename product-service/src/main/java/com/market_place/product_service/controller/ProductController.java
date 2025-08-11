package com.market_place.product_service.controller;

import com.market_place.product_service.dto.ProductRequestDto;
import com.market_place.product_service.dto.ProductResponseDto;
import com.market_place.product_service.dto.QuantityUpdateDto;
import com.market_place.product_service.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    // hasRole('VENDITORE' and 'ADMIN')
    @PostMapping("/")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductRequestDto request){
        return ResponseEntity.status(201).body(productService.create(request));
    }

    // hasRole('VENDITORE' and 'ADMIN' and 'ACQUIRENTE')
    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDto>> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    @PutMapping("/")
    public ResponseEntity<ProductResponseDto> updateQuantity(@RequestBody @Valid QuantityUpdateDto request){
        return ResponseEntity.ok(productService.updateQuantity(request));
    }
}
