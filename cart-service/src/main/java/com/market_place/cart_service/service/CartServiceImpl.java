package com.market_place.cart_service.service;

import com.market_place.cart_service.controller.ProductClient;
import com.market_place.cart_service.dto.*;
import com.market_place.cart_service.exception.CartNotFoundEx;
import com.market_place.cart_service.exception.ProductNotFoundEx;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductClient productExchangeController;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    /**
     * Aggiunta prodotto al carrello
     *
     * <p> Il metodo esegue i seguenti step:</p>
     *
     * <ul>
     *     <li> Chiamo il metodo purchase del product-service, che mi ritorna il Prodotto in DTO </li>
     *     <li> Verifico che il prodotto non sia null, in caso lancio un eccezione </li>
     *     <li> Ottengo il cart dell'utente e se non esiste lo creo e lo salvo nel database </li>
     *     <li> Creo il Cart Item attraverso il DTO del prodotto solo con i dati essenziali, lo salvo nel db </li>
     *     <li> Aggiungo il prodotto al cart dell'utente e lo salvo nel db </li>
     * </ul>
     * @param productId id del prodotto
     * @param request quantità richiesta da inserire nel carrello
     * @return un DTO con i dati del carrello modificato
     */
    @Override
    @Transactional
    public CartUpdateResponseDto addProduct(Long productId, PurchaseRequestDto request) {
        UUID userId = getUserId();
        log.info("[ADD PRODUCT] Aggiunta del prodotto {} al cart dell'user {}", productId, userId);

        ProductResponseDto product = productExchangeController.purchaseProduct(productId, request);

        log.info("{}", product);

        if (product == null){
            log.warn("[ADD PRODUCT] Prodotto {} non trovato", productId);
            throw new ProductNotFoundEx("Prodotto non trovato");
        }

        Cart userCart = cartRepository.findByUserOwner(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .userOwner(userId)
                        .cartItem(new ArrayList<>())
                        .build()
                        ));

        CartProduct cartProduct = CartProduct.builder()
                .productId(product.getId())
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
                .quantity(savedProduct.getQuantity())
                .build();
    }

    /**
     * Rimozione prodotto dal carrello
     *
     * <p> Il metodo esegue i seguenti step: </p>
     *
     * <ul>
     *     <li> Ottengo il cart dell'utente, se non esiste lancio un eccezione </li>
     *     <li> Ottengo il prodotto dal cart dell'utente, dato il parametro id, se non esiste lancio un eccezione </li>
     *     <li> Rimuovo il prodotto e controllo che sia andato a buon fine </li>
     *     <li> Aggiorno la quantità del prodotto nel product-service </li>
     *     <li> Salvo il cart dell'utente </li>
     * </ul>
     * @param productId per ottenere il prodotto
     * @throws CartNotFoundEx se il carrello non esiste
     * @throws ProductNotFoundEx se il prodotto non esiste
     * @return un DTO per mostrare il carrello aggiornato
     */
    @Override
    @Transactional
    public CartUpdateResponseDto remove(Long productId) {
        UUID userId =  getUserId();

        Cart userCart = cartRepository.findByUserOwner(userId)
                .orElseThrow(() -> new CartNotFoundEx("Carrello non trovato"));

        CartProduct product = cartProductRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundEx("Prodotto non trovato"));

        boolean removed = userCart.getCartItem().removeIf(
                ci -> ci.getProductId().equals(productId)
        );
        if (!removed){
            throw new ProductNotFoundEx("Impossibile rimuovere il prodotto dal carrello");
        }

        productExchangeController.updateQuantity(QuantityUpdateDto.builder()
                .productId(product.getProductId())
                .quantity(product.getQuantity())
                .type(UpdateType.ADD)
                .build());

        cartRepository.save(userCart);

        return CartUpdateResponseDto.builder()
                .message("Prodotto rimosso con successo al carrello")
                .cartId(userCart.getId())
                .productId(product.getProductId())
                .userId(userId)
                .quantity(product.getQuantity())
                .build();
        
    }

    /**
     * Ottiene il carrello con i prodotti dell'utente
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo il carrello dell'utente e creo un DTO di risposta, se non esiste lancio un eccezione </li>
     *     <li> Per ogni prodotto al suo interno, creo un DTO di risposta </li>
     * </ul>
     * @throws CartNotFoundEx carrello non esistente
     * @return
     */
    @Override
    public CartDto getUserCart() {
        UUID userId = getUserId();
        log.info("[GET CART] userId {}", userId);

        return cartRepository.findByUserOwner(userId).stream()
                .map(cart -> CartDto.builder()
                        .id(cart.getId())
                        .ownerId(userId)
                        .cartItems(cart.getCartItem().stream()
                                .map(item -> CartItemDto.builder()
                                        .id(item.getId())
                                        .productId(item.getProductId())
                                        .name(item.getName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .build())
                                .toList())
                        .build())
                .findFirst()
                .orElseThrow(() -> new CartNotFoundEx("Carrello non trovato"));
    }

    /**
     * Calcolo totale del carrello
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo il carrello dell'utente, se non esiste lancio un eccezione </li>
     *     <li> Per ogni item del carrello, ottengo il suo prezzo, lo moltiplico per la quantità e lo sommo</li>
     * </ul>
     * @return prezzo totale
     * @throws CartNotFoundEx se il carrello non esiste
     */
    @Override
    public Double getTotalPrice() {
        UUID userId = getUserId();

        Cart userCart = cartRepository.findByUserOwner(userId)
                .orElseThrow(() -> new CartNotFoundEx("Carrello non trovato"));

        return userCart.getCartItem().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }


    private static UUID getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
