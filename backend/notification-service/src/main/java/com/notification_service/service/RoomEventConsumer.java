package com.notification_service.service;

import com.notification_service.event.RoomEvent;
import com.notification_service.handler.AppWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomEventConsumer {

    private final AppWebSocketHandler handler;

    @KafkaListener(topics = "room-events", groupId = "notification-service", containerFactory = "kafkaListenerContainerFactory")
    public void read(RoomEvent event) {
        log.info("Received RoomEvent: {}", event);

        String payload = switch (event.eventType()) {
            case "USER_JOINED_ROOM" -> "User " + event.userId() + " joined room " + event.roomId();
            case "USER_LEFT_ROOM" -> "User " + event.userId() + " left room " + event.roomId();
            case "MESSAGE_SENT" -> "New message from " + event.userId() + " in room " + event.roomId();
            default -> null;
        };

        if (payload != null && event.participants() != null) {
            for (String userId : event.participants()) {
                if (!userId.equals(event.userId())) {
                    handler.sendToUser(userId, payload);
                }
            }
        }
    }
}
