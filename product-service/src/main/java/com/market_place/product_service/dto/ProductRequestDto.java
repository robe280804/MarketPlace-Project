package com.market_place.product_service.dto;

import com.market_place.product_service.model.Category;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Il nome del prodotto è obbligatorio.")
    @Size(min = 2, max = 100, message = "Il nome deve avere tra {min} e {max} caratteri.")
    private String name;

    @NotBlank(message = "L'URL dell'immagine è obbligatorio.")
    @Pattern(
            regexp = "^(https?://.*\\.(?:png|jpg|jpeg|gif))$",
            message = "L'immagine deve essere un URL valido che punta a un file PNG, JPG, JPEG o GIF."
    )
    private String image;

    @NotBlank(message = "La descrizione è obbligatoria.")
    @Size(min = 10, max = 2000, message = "La descrizione deve avere tra {min} e {max} caratteri.")
    private String description;

    @NotNull(message = "La quantità è obbligatoria.")
    @Min(value = 0, message = "La quantità non può essere negativa.")
    private Integer quantity;

    @NotNull(message = "Il prezzo è obbligatorio.")
    @DecimalMin(value = "0.01", inclusive = true, message = "Il prezzo deve essere almeno {value}.")
    @Digits(integer = 10, fraction = 2, message = "Il prezzo deve avere al massimo 2 decimali.")
    private Double price;

    @NotNull(message = "La categoria è obbligatoria.")
    private Category category;
}
