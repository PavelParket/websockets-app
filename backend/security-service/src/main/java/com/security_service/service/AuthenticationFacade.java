package com.security_service.service;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.AuthResponse;
import com.security_service.dto.RegisterRequest;
import com.security_service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationFacade {

    private final UserService userService;

    private final AuthService authService;

    public AuthResponse register(RegisterRequest request) {
        UserResponse response = userService.create(request);

        return authService.register(response.email());
    }

    public AuthResponse login(AuthRequest request) {
        return authService.login(request);
    }

    public AuthResponse refresh(String token) {
        return authService.refresh(token);
    }
}
