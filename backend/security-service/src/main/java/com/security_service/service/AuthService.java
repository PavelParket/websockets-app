package com.security_service.service;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.AuthResponse;
import com.security_service.entity.User;
import com.security_service.exception.InvalidCredentialsException;
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

    private final TokenService tokenService;

    private final AuthenticationManager manager;

    public AuthResponse register(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String accessToken = tokenService.generateAccess(email, user.getRole().toString(), user.getUsername());
        String refreshToken = tokenService.generateRefresh(email, user.getRole().toString(), user.getUsername());

        return mapper.toAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest request) {
        try {
            User user = repository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.email()));

            authenticate(request.email(), request.password());

            String accessToken = tokenService.generateAccess(user.getEmail(), user.getRole().toString(), user.getUsername());
            String refreshToken = tokenService.generateRefresh(user.getEmail(), user.getRole().toString(), user.getUsername());

            return mapper.toAuthResponse(user, accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    public AuthResponse refresh(String token) {
        return tokenService.refresh(token, repository, mapper);
    }

    private void authenticate(String email, String password) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
