package com.market_place.cart_service.service;

import com.market_place.cart_service.controller.ProductClient;
import com.market_place.cart_service.dto.CartUpdateResponseDto;
import com.market_place.cart_service.dto.ProductResponseDto;
import com.market_place.cart_service.model.Cart;
import com.market_place.cart_service.model.CartProduct;
import com.market_place.cart_service.repository.CartProductRepository;
import com.market_place.cart_service.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductClient productExchangeController;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    /// ATT -> MANCA LA PARTE DELLA LOGICA DELLA QUANTITA
    /// Aggiunta prodotto al carrello
    /// -> attraverso l'id del prodotto chiamo il microservice product-service e ottengo il prodotto
    /// -> ottengo il Cart dell'utente, se non esiste lo creo e lo salvo nel database
    /// -> creo il CartProduct attraverso il prodotto ottenuto e lo salvo nel database
    /// -> aggiungo al Cart dell'utente il CartProduct e lo salvo nel database
    @Override
    @Transactional
    public CartUpdateResponseDto addProduct(Long productId) {
        UUID userId = getUserId();
        log.info("[ADD PRODUCT] Aggiunta del prodotto {} al cart dell'user {}", productId, userId);

        ProductResponseDto product = productExchangeController.getProduct(productId);

        if (product == null){
            log.warn("[ADD PRODUCT] Prodotto {} non trovato", productId);
            throw new EntityNotFoundException("Prodotto non trovato");
        }

        Cart userCart = cartRepository.findByUserOwner(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .userOwner(userId)
                        .cartItem(new ArrayList<>())
                        .build()
                        ));

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

        log.info("[ADD PRODUCT] Prodotto {} aggiunto con succeesso al Cart con id {} dell'utente {}",
                productId, userCart.getId(), userId);

        return CartUpdateResponseDto.builder()
                .message("Prodotto aggiunto con successo al carrello")
                .cartId(userCart.getId())
                .productId(productId)
                .userId(userId)
                .quantity(null)
                .build();
    }



    private static UUID getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
