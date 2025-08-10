package com.market_place.product_service.dto;

import com.market_place.product_service.model.Category;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponseDto {

    private Long id;
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private Category category;
    private LocalDateTime createdAt;
}
