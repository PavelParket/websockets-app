package com.security_service.service;

import com.security_service.dto.AuthResponse;
import com.security_service.entity.User;
import com.security_service.exception.InvalidCredentialsException;
import com.security_service.jwt.JwtProvider;
import com.security_service.jwt.factory.AccessTokenFactory;
import com.security_service.jwt.factory.RefreshTokenFactory;
import com.security_service.mapper.UserMapper;
import com.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtProvider provider;

    private final AccessTokenFactory accessFactory;

    private final RefreshTokenFactory refreshFactory;

    public String generateAccess(String email, String role, String username) {
        return accessFactory.generateToken(email, role, username);
    }

    public String generateRefresh(String email, String role, String username) {
        return refreshFactory.generateToken(email, role, username);
    }

    public AuthResponse refresh(String token, UserRepository repository, UserMapper mapper) {
        if (!provider.validate(token)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String email = provider.getEmail(token);
        String role = provider.getRole(token);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = accessFactory.generateToken(email, role, user.getUsername());
        String refreshToken = refreshFactory.generateToken(email, role, user.getUsername());

        return mapper.toAuthResponse(user, accessToken, refreshToken);
    }
}
