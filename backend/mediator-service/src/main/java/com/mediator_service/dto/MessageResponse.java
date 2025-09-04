package com.mediator_service.dto;

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
