package com.market_place.auth_service.dto;

import com.market_place.auth_service.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequestDto {

    @Email(message = "Email non valida")
    @NotBlank(message = "L'email non può essere vuota")
    private String email;

    @NotNull(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve essere lunga almeno 6 caratteri")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;
    private Set<Role> roles;
}
