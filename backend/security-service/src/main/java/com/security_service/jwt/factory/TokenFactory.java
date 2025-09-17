package com.security_service.jwt.factory;

public interface TokenFactory {

    String generateToken(String email, String role, String username);
}
