package com.market_place.auth_service.service;

import com.market_place.auth_service.dto.RegisterRequestDto;
import com.market_place.auth_service.dto.RegisterResponseDto;
import jakarta.validation.Valid;

public interface AuthService {
    RegisterResponseDto register(@Valid RegisterRequestDto request);
}
