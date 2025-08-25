package com.market_place.auth_service.service;

import com.market_place.auth_service.dto.LoginRequestDto;
import com.market_place.auth_service.dto.LoginResponseDto;
import com.market_place.auth_service.dto.RegisterRequestDto;
import com.market_place.auth_service.dto.RegisterResponseDto;
import jakarta.validation.Valid;

import java.util.Map;

public interface AuthService {
    RegisterResponseDto register(@Valid RegisterRequestDto request);

    LoginResponseDto login(@Valid LoginRequestDto request);

    Map<String, Object> validateToken(String authHeader);
}
