package com.security_service.dto;

import lombok.Builder;

@Builder
public record AuthRequest(
        String email,
        String password
) {
}
