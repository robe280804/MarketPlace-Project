package com.market_place.auth_service.controller;

import com.market_place.auth_service.dto.RegisterRequestDto;
import com.market_place.auth_service.dto.RegisterResponseDto;
import com.market_place.auth_service.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request){
        return ResponseEntity.status(201).body(authService.register(request));
    }
}
