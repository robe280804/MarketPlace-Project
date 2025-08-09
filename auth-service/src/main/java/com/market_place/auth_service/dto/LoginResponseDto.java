package com.market_place.auth_service.dto;

import com.market_place.auth_service.model.AuthProvider;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoginResponseDto {

    private String message;
    private UUID userId;
    private String email;
    //private Collection<? extends GrantedAuthority> roles;
    private AuthProvider provider;
    private String token;
}
