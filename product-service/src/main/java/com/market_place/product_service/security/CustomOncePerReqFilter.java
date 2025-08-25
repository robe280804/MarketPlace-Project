package com.market_place.product_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomOncePerReqFilter extends OncePerRequestFilter {

    /// Creo l' authentication attraverso i dati inviati nell' header dal gateway
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        UUID userId = UUID.fromString(request.getHeader("x-User-Id"));
        String roles = request.getHeader("X-User-Roles");

        List<String> userRoles = Arrays.stream(roles.split(",")).toList();

        log.info("[FILTRO ONCE PER REQUEST] User id {}, ruoli {}", userId, userRoles);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userId,
                null,
                userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        ));

        filterChain.doFilter(request, response);
    }
}
