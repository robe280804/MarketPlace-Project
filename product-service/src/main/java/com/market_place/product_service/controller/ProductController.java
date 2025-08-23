package com.market_place.product_service.controller;

import com.market_place.product_service.dto.ProductRequestDto;
import com.market_place.product_service.dto.ProductResponseDto;
import com.market_place.product_service.dto.PurchaseRequestDto;
import com.market_place.product_service.dto.QuantityUpdateDto;
import com.market_place.product_service.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductRequestDto request){
        return ResponseEntity.status(201).body(productService.create(request));
    }

    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN') or hasRole('ACQUIRENTE')")
    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDto>> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    /// RIMUOVE ANCHE LA QUANTITA
    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN') or hasRole('ACQUIRENTE')")
    @PostMapping("/{productId}/purchase")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId,
                                                         @RequestBody PurchaseRequestDto request){
        return ResponseEntity.ok(productService.getProduct(productId, request));
    }

    //Un venditore può aggiornare il suo prodotto, lo stesso vale per l' admin
    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<ProductResponseDto> updateQuantity(@RequestBody @Valid QuantityUpdateDto request){
        return ResponseEntity.ok(productService.updateQuantity(request));
    }

    //Solo chi ha creato il prodotto o l'admin possono eliminarlo
    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();  //204
    }

    @PreAuthorize("hasRole('VENDITORE') or hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<ProductResponseDto>> getUserProducts(){
        return ResponseEntity.ok(productService.getUserProducts());
    }
}
