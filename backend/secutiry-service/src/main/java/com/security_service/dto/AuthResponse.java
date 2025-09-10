package com.security_service.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String role,
        String accessToken,
        String refreshToken
) {
}
