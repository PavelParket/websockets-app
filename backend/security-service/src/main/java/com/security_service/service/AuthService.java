package com.security_service.service;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.AuthResponse;
import com.security_service.entity.User;
import com.security_service.exception.InvalidCredentialsException;
import com.security_service.jwt.factory.AccessTokenFactory;
import com.security_service.jwt.factory.RefreshTokenFactory;
import com.security_service.mapper.UserMapper;
import com.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final AccessTokenFactory accessFactory;

    private final RefreshTokenFactory refreshFactory;

    private final AuthenticationManager manager;

    public AuthResponse register(String email, String role) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String accessToken = accessFactory.generateToken(email, role);
        String refreshToken = refreshFactory.generateToken(email, role);

        return mapper.toAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest request) {
        try {
            User user = repository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.email()));

            authenticate(request.email(), request.password());

            String accessToken = accessFactory.generateToken(user.getEmail(), user.getRole().toString());
            String refreshToken = refreshFactory.generateToken(user.getEmail(), user.getRole().toString());

            return mapper.toAuthResponse(user, accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private void authenticate(String email, String password) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
