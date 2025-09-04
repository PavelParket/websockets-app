package com.mediator_service.dto;

import lombok.Builder;

@Builder
public record MessageRequest(
        String type,
        String toUserId,
        String content
) {
}
