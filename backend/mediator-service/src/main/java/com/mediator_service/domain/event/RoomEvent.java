package com.mediator_service.domain.event;

import com.mediator_service.domain.enums.EventType;

import java.util.List;

public record RoomEvent(
        EventType eventType,
        String roomId,
        String userId,
        List<String> participants
) {
}
