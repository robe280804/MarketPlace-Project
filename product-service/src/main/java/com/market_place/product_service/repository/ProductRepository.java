package com.market_place.product_service.repository;

import com.market_place.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/// NOTA: query fa riferimento alla classe e ai suoi campi, non alle colonne nel db
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.quantity = p.quantity + :quantity " +
            "WHERE p.id = :id AND p.creatorId = :userId")
    int incrementQuantity(@Param("id")Long id, @Param("quantity") Integer quantity, @Param("userId") UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity " +
            "WHERE p.id = :id AND p.quantity >= :quantity AND p.creatorId = :userId")
    int decrementQuantity(@Param("id")Long id, @Param("quantity") Integer quantity, @Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    int deleteByIdCustom(@PathVariable("id")Long id);

    int deleteByIdAndCreatorId(Long id, UUID userId);

    List<Product> findByCreatorId(UUID userId);
}
