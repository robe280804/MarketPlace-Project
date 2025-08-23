package com.market_place.cart_service.repository;

import com.market_place.cart_service.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
    Optional<CartProduct> findByProductId(Long productId);
}
