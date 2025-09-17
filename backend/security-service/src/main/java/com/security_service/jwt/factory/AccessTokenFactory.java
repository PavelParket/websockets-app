package com.security_service.jwt.factory;

import com.security_service.jwt.JwtProperties;
import com.security_service.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenFactory implements TokenFactory {

    private final JwtProperties properties;

    private final JwtProvider provider;

    @Override
    public String generateToken(String email, String role, String username) {
        return provider.generateToken(email, role, username, properties.accessExpiration());
    }
}
