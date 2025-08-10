package com.market_place.product_service.service;

import com.market_place.product_service.dto.ProductRequestDto;
import com.market_place.product_service.dto.ProductResponseDto;
import com.market_place.product_service.mapper.ProductMapper;
import com.market_place.product_service.model.Product;
import com.market_place.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
        log.info("[CREATE] Creazione prodotto");

        Product newProduct = Product.builder()
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
        return productRepository.findAll().stream()
                .map(mapper::fromProductToDto)
                .collect(Collectors.toList());
    }
}
