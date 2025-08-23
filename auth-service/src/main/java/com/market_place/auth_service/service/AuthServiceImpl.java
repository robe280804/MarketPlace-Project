package com.market_place.auth_service.service;

import com.market_place.auth_service.dto.LoginRequestDto;
import com.market_place.auth_service.dto.LoginResponseDto;
import com.market_place.auth_service.exception.UserAlreadyRegisterEx;
import com.market_place.auth_service.dto.RegisterRequestDto;
import com.market_place.auth_service.dto.RegisterResponseDto;
import com.market_place.auth_service.model.AuthProvider;
import com.market_place.auth_service.model.User;
import com.market_place.auth_service.repository.UserRepository;
import com.market_place.auth_service.security.JwtService;
import com.market_place.auth_service.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Registrazione dell'utente
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Verifico che l'utente non è gia registrato nel sistema </li>
     *     <li> Costruisco l'entità user eseguendo l' hashing della password </li>
     *     <li> Salvo l'utente nel db e creo un DTO di risposta con i dati necessari alla sua registrazione </li>
     * </ul>
     * @param request DTO con i dati per registrare l'utente
     * @return un DTO con i dati della registrazione dell'utente
     * @throws UserAlreadyRegisterEx se l'utente è già registrato
     */
    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {
        log.info("[REGISTER] Registrazione in esecuzione per {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())){
            log.warn("[REGISTER] Registrazione fallita per {}, email già in uso", request.getEmail());
            throw new UserAlreadyRegisterEx("Utente gia registrato");
        }

        log.info("ruoli {}", request.getRoles());

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .provider(AuthProvider.LOCALE)
                .build();

        log.info("user: {}", user);

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

    /**
     * Login dell'utente
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Esegue l'autenticazione attraverso l' authentication manager, passando i dati inseriti nel DTO </li>
     *     <li> Se l'autenticazione va a buon fine, la salvo nel SecurityContextHolder </li>
     *     <li> Creo il jwt Token attraverso la classe che implementa UserDetails </li>
     * </ul>
     * @param request DTO per eseguire il login dell'utente
     * @return un DTO con i dati del login dell'utente
     * @throws BadCredentialsException se l'utente inserisce le credenziali errate
     */
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        log.info("[LOGIN] Login in esecuzione per {}", request.getEmail());

        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
        } catch (BadCredentialsException ex) {
            log.warn("[LOGIN] Fallito per email {}: {}", request.getEmail(), ex.getMessage());
            throw new BadCredentialsException("Credenziali errate");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtService.generateToken(
                userDetails.getId(), userDetails.getEmail(), userDetails.getAuthorities(), userDetails.getProvider()
        );
        log.info("[LOGIN] Login eseguito per {}", request.getEmail());

        return LoginResponseDto.builder()
                .message("Login andato a buon fine")
                .userId(userDetails.getId())
                .email(userDetails.getEmail())
                .provider(userDetails.getProvider())
                .roles(userDetails.getAuthorities())
                .token(token)
                .build();

    }
}
