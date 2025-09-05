package com.mediator_service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediator_service.dto.MessageResponse;
import com.mediator_service.service.RoomManager;
import com.mediator_service.service.SessionManager;
import com.mediator_service.service.SystemMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;

    private final RoomManager roomManager;

    private final List<MessageHandler> handlers;

    private final SystemMessageService systemMessageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        String roomId = getRoomIdFromSession(session);

        sessionManager.register(userId, session);
        roomManager.addSession(roomId, session);

        systemMessageService.userJoined(roomId, userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            MessageResponse incoming = new ObjectMapper().readValue(message.getPayload(), MessageResponse.class);

            handlers.stream()
                    .filter(messageHandler -> messageHandler.supports(incoming.type()))
                    .findFirst()
                    .ifPresentOrElse(
                            messageHandler -> messageHandler.handle(incoming, session),
                            () -> log.warn("No handler found for type {}", incoming.type())
                    );
        } catch (Exception e) {
            log.error("Failed to handle message", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserIdFromSession(session);
        String roomId = getRoomIdFromSession(session);

        sessionManager.remove(userId);
        roomManager.removeSession(roomId, session);

        systemMessageService.userLeft(roomId, userId);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    private String getRoomIdFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }

    @Deprecated
    private void sendMessage(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
