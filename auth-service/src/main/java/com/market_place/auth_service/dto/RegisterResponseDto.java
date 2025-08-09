package com.market_place.auth_service.dto;

import com.market_place.auth_service.model.AuthProvider;
import com.market_place.auth_service.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class RegisterResponseDto {

    private String message;
    private UUID userId;
    private String email;
    private Set<Role> roles;
    private AuthProvider provider;
    private LocalDateTime createdAt;
}
