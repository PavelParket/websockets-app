package com.security_service.service;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.AuthResponse;
import com.security_service.dto.CreateUserRequest;
import com.security_service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationFacade {

    private final UserService userService;

    private final AuthService authService;

    public AuthResponse register(CreateUserRequest request) {
        UserResponse response = userService.create(request);

        return authService.register(response.email(), response.role());
    }

    public AuthResponse login(AuthRequest request) {
        return authService.login(request);
    }
}
