package com.market_place.cart_service.service;

import com.market_place.cart_service.controller.ProductExchangeController;
import com.market_place.cart_service.dto.ProductResponseDto;
import com.market_place.cart_service.model.Cart;
import com.market_place.cart_service.model.CartProduct;
import com.market_place.cart_service.repository.CartProductRepository;
import com.market_place.cart_service.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductExchangeController productExchangeController;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    @Override
    public ResponseEntity<CartUpdateResponseDto> addProduct(Long productId) {
        UUID userId = getUserId();

        ProductResponseDto product = productExchangeController.getProduct(productId);

        if (product == null){
            throw new EntityNotFoundException("Prodotto non trovato");
        }

        Cart userCart = cartRepository.findByUserOwner(userId)
                .orElseGet(() -> Cart.builder()
                        .userOwner(userId)
                        .cartItem(new ArrayList<>())
                        .build()
                        );

        CartProduct cartProduct = CartProduct.builder()
                .name(product.getName())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .removeTime(null)
                .cart(userCart)
                .build();

        CartProduct savedProduct = cartProductRepository.save(cartProduct);
        cartProductRepository.flush();

        userCart.getCartItem().add(savedProduct);
        cartRepository.save(userCart);
    }



    private static UUID getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
