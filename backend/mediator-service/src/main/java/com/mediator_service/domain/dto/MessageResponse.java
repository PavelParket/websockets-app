package com.mediator_service.domain.dto;

import lombok.Builder;

@Builder
public record MessageResponse(
        String type,
        String fromUserId,
        String toUserId,
        String roomId,
        String content
) {
}
