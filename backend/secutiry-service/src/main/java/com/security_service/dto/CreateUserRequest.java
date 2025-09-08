package com.security_service.dto;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String username,
        String email,
        String password
) {
}
