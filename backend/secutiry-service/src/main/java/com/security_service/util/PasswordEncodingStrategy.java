package com.security_service.util;

public interface PasswordEncodingStrategy {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodingPassword);
}
