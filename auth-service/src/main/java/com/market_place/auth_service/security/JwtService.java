package com.market_place.auth_service.security;

import com.market_place.auth_service.model.AuthProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    private Key convertKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(convertKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    public String generateToken(UUID userId, String email, Collection<? extends GrantedAuthority> authorities, AuthProvider provider){
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("sub", userId);
        claims.put("email", email);
        claims.put("provider", provider);
        return createToken(claims);
    }

    public Claims estraiAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(convertKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID estraiUserId(String token) {
        return UUID.fromString(estraiAllClaims(token).getSubject());
    }

    public String estraiEmail(String token) {
        return estraiAllClaims(token).get("email", String.class);
    }

    public List<String> estraiAuthorities(String token){
        return estraiAllClaims(token).get("authorities", List.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = estraiEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date estraiExpiration(String token){
        return  estraiAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return estraiExpiration(token).before(new Date());
    }
}

