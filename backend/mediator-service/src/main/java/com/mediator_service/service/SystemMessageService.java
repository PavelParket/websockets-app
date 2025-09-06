package com.mediator_service.service;

import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.factory.MessageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemMessageService {

    private final RoomManager roomManager;

    private final MessageFactory factory;

    public void userJoined(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " joined the room");
    }

    public void userLeft(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " left the room");
    }

    private void broadcast(String roomId, String content) {
        MessageResponse message = factory.toSystemMessage(roomId, content);
        roomManager.broadcast(roomId, message, null);
    }
}
