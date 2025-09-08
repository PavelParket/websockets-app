package com.security_service.service;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.CreateUserRequest;
import com.security_service.dto.UserResponse;
import com.security_service.entity.Role;
import com.security_service.entity.User;
import com.security_service.exception.InvalidCredentialsException;
import com.security_service.jwt.JwtProvider;
import com.security_service.mapper.UserMapper;
import com.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;

    private final UserService service;

    private final UserMapper mapper;

    private final JwtProvider provider;

    private final AuthenticationManager manager;

    public UserResponse register(CreateUserRequest request) {
        return service.create(request);
    }

    public UserResponse login(AuthRequest request) {
        try {
            User user = repository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.email()));

            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
            );

            String accessToken = provider.generateAccess(authentication.getName(), Role.USER.toString());

            return mapper.toResponse(user, accessToken);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
