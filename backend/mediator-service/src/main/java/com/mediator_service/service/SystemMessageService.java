package com.mediator_service.service;

import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.domain.enums.EventType;
import com.mediator_service.domain.event.RoomEvent;
import com.mediator_service.factory.MessageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SystemMessageService {

    private final RoomManager roomManager;

    private final MessageFactory factory;

    private final RoomEventProducer producer;

    public void userJoined(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " joined the room");

        RoomEvent event = new RoomEvent(EventType.USER_JOINED_ROOM, roomId, userId, new ArrayList<>(roomManager.getUsersId(roomId)));
        producer.send(event);
    }

    public void userLeft(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " left the room");

        RoomEvent event = new RoomEvent(EventType.USER_LEFT_ROOM, roomId, userId, new ArrayList<>(roomManager.getUsersId(roomId)));
        producer.send(event);
    }

    private void broadcast(String roomId, String content) {
        MessageResponse message = factory.toSystemMessage(roomId, content);
        roomManager.broadcast(roomId, message, null);
    }
}
