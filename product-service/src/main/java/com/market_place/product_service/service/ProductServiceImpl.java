package com.market_place.product_service.service;

import com.market_place.product_service.dto.ProductRequestDto;
import com.market_place.product_service.dto.ProductResponseDto;
import com.market_place.product_service.dto.QuantityUpdateDto;
import com.market_place.product_service.dto.UpdateType;
import com.market_place.product_service.mapper.ProductMapper;
import com.market_place.product_service.model.Product;
import com.market_place.product_service.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    /// Quando aggiungo la security affidare al prodotto l'id di chi lo crea e il tipo (ADMIN, VENDITORE)
    @Override
    public ProductResponseDto create(ProductRequestDto request) {
        UUID userId = getUserId();
        log.info("[CREATE] Creazione prodotto");

        Product newProduct = Product.builder()
                .creatorId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(request.getCategory())
                .build();

        Product savedProduct = productRepository.save(newProduct);
        log.info("[CREATE] Creazione prodotto {} con nome: {} andata a buon fine", savedProduct.getId(), savedProduct.getName());

        return mapper.fromProductToDto(savedProduct);
    }

    @Override
    public List<ProductResponseDto> getProducts() {
        UUID userId = getUserId();
        log.info("[GET PRODUCTS] Visualizzazione dei prodotti da parte di {}", userId);

        return productRepository.findAll().stream()
                .map(mapper::fromProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDto updateQuantity(QuantityUpdateDto request) {
        UUID userId = getUserId();
        log.info("[UPDATE] Utente {} modifica della quantità per il prodotto {}",userId, request.getProductId());

        int rows;

        if (request.getType().equals(UpdateType.ADD)){
            rows = productRepository.incrementQuantity(request.getProductId(), request.getQuantity(), userId);
        } else {
            rows = productRepository.decrementQuantity(request.getProductId(), request.getQuantity(), userId);
        }
        if (rows == 0) {
            throw new EntityNotFoundException("Prodotto non trovato o quantità insufficiente");
        }
        Product updatedProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato dopo aggiornamento"));

        log.info("[UPDATE] Modifica del prodotto {} andata a buon fine, quantità attuale: {}",
                updatedProduct.getId(), updatedProduct.getQuantity());

        return mapper.fromProductToDto(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = (UUID) auth.getPrincipal();

        log.info("[DELETE] User {} sta cercando di eliminare il prodotto {}", userId, id);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        int rows;
        if (isAdmin) {
            rows = productRepository.deleteByIdCustom(id);
        } else {
            rows = productRepository.deleteByIdAndCreatorId(id, userId);
        }
        if (rows == 0){
            log.warn("[DELETE] Prodotto con id {} non trovato o errore interno", id);
            throw new EntityNotFoundException("Elemento non esistente");
        }
    }

    @Override
    public List<ProductResponseDto> getUserProducts() {
        log.info("[GET USER PRODUCTS] Visualizzazione dei prodotti dell'utente {}", getUserId());

        return productRepository.findByCreatorId(getUserId()).stream()
                .map(mapper::fromProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {
        UUID userId = getUserId();
        log.info("[GET PRODUCT] Visualizzazione prodotto con id {} da utente {}", userId, productId);

        return productRepository.findById(productId).map(mapper::fromProductToDto)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));
    }

    private static UUID getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
