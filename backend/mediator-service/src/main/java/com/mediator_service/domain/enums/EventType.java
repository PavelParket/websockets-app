package com.mediator_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

    USER_JOINED_ROOM("User joined the room."),
    USER_LEFT_ROOM("User left the room."),
    MESSAGE_SENT("User sent a message.");

    private final String eventType;
}
