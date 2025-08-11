package com.market_place.product_service.repository;

import com.market_place.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/// NOTA: query fa riferimento alla classe e ai suoi campi, non alle colonne nel db
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.quantity = p.quantity + :quantity WHERE p.id = :id")
    Integer incrementQuantity(@Param("id")Long id, @Param("quantity") Integer quantity);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :id AND p.quantity >= :quantity")
    Integer decrementQuantity(@Param("id")Long id, @Param("quantity") Integer quantity);
}
