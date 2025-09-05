package com.mediator_service.domain.dto;

import lombok.Builder;

@Builder
public record MessageRequest(
        String type,
        String toUserId,
        String content
) {
}
