package com.market_place.auth_service.service;

import com.market_place.auth_service.UserAlredyRegisterEx;
import com.market_place.auth_service.dto.RegisterRequestDto;
import com.market_place.auth_service.dto.RegisterResponseDto;
import com.market_place.auth_service.model.AuthProvider;
import com.market_place.auth_service.model.User;
import com.market_place.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {
        log.info("[REGISTER] Registrazione in esecuzione per {}", request.getEmail());

        if (userRepository.exitsByEmail(request.getEmail())){
            log.warn("[REGISTER] Registrazione fallita per {}, email già in uso", request.getEmail());
            throw new UserAlredyRegisterEx("Utente gia registrato");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .provider(AuthProvider.LOCALE)
                .build();

        User savedUser = userRepository.save(user);
        userRepository.flush();

        log.info("[REGISTER] Registrazione eseguita per {}", request.getEmail());

        return RegisterResponseDto.builder()
                .message("Registrazione andata a buon fine")
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles())
                .provider(savedUser.getProvider())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }
}
