package com.security_service.controller;

import com.security_service.dto.AuthRequest;
import com.security_service.dto.CreateUserRequest;
import com.security_service.dto.UserResponse;
import com.security_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody AuthRequest request) {
        return new ResponseEntity<>(service.login(request), HttpStatus.OK);
    }
}
