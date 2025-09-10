package com.security_service.jwt.factory;

import com.security_service.jwt.JwtProperties;
import com.security_service.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenFactory implements TokenFactory {

    private final JwtProperties properties;

    private final JwtProvider provider;

    @Override
    public String generateToken(String email, String role) {
        return provider.generateToken(email, role, properties.refreshExpiration());
    }
}
