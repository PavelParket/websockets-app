package com.security_service.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long id,
        String username,
        String email,
        String role,
        LocalDate createdAt
) {
}
