package com.mediator_service.service;

import com.mediator_service.domain.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemMessageService {

    private final RoomManager roomManager;

    public void userJoined(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " joined the room");
    }

    public void userLeft(String roomId, String userId) {
        broadcast(roomId, "User " + userId + " left the room");
    }

    private void broadcast(String roomId, String content) {
        MessageResponse message = new MessageResponse("system", "system", "all", roomId, content);
        roomManager.broadcast(roomId, message, null);
    }
}
