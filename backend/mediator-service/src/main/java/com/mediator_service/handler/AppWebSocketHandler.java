package com.mediator_service.handler;

import com.mediator_service.service.RoomManager;
import com.mediator_service.service.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;

    private final RoomManager roomManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);

        sessionManager.register(userId, session);

        roomManager.addSession("default", session);

        sendMessage(session, "Hello");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        roomManager.broadcast("default", message, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);

        sessionManager.remove(userId);

        roomManager.removeSession("default", session);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

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
