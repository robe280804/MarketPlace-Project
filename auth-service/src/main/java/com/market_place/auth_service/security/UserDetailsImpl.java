package com.market_place.auth_service.security;

import com.market_place.auth_service.model.AuthProvider;
import com.market_place.auth_service.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {

    private final UUID id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final AuthProvider provider;

    public UserDetailsImpl(User user) {
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        this.provider = user.getProvider();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.id = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
