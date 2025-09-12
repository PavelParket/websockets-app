package com.notification_service.event;

import java.util.List;

public record RoomEvent(
        String eventType,
        String roomId,
        String userId,
        List<String> participants
) {
}
