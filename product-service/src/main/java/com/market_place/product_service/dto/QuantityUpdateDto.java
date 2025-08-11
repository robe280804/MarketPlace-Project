package com.market_place.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuantityUpdateDto {

    @NotNull(message = "L'id del prodotto è obbligatorio")
    private Long productId;

    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 1, message = "La quantità deve essere almeno 1")
    private Integer quantity;

    @NotNull(message = "Il tipo di aggiornamento è obbligatorio")
    private UpdateType type;
}
