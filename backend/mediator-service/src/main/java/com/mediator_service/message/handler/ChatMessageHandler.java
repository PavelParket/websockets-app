package com.mediator_service.message.handler;

import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.service.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageHandler implements MessageHandler {

    private final RoomManager roomManager;

    @Override
    public boolean supports(String type) {
        return "message".equals(type);
    }

    @Override
    public void handle(MessageResponse message, WebSocketSession session) {
        String roomId = (String) session.getAttributes().get("roomId");
        String userId = (String) session.getAttributes().get("userId");
        roomManager.broadcast(roomId, message, session);

        log.info("Processed chat message from {} in room \"{}\"", message.fromUserId(), roomId);
    }
}
