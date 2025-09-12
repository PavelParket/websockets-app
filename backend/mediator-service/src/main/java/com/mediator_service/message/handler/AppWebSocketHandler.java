package com.mediator_service.message.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediator_service.domain.dto.MessageRequest;
import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.domain.enums.EventType;
import com.mediator_service.domain.event.RoomEvent;
import com.mediator_service.factory.MessageFactory;
import com.mediator_service.service.RoomEventProducer;
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
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;

    private final RoomManager roomManager;

    private final List<MessageHandler> handlers;

    private final SystemMessageService systemMessageService;

    private final MessageFactory factory;

    private final RoomEventProducer producer;

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
            MessageRequest request = new ObjectMapper().readValue(message.getPayload(), MessageRequest.class);
            MessageResponse response = factory.toMessage(request, session);

            handlers.stream()
                    .filter(messageHandler -> messageHandler.supports(response.type()))
                    .findFirst()
                    .ifPresentOrElse(
                            messageHandler -> {
                                messageHandler.handle(response, session);

                                RoomEvent event = new RoomEvent(EventType.MESSAGE_SENT, response.roomId(), response.fromUserId(), new ArrayList<>(roomManager.getUsersId(response.roomId())));
                                producer.send(event);
                            },
                            () -> log.warn("No handler found for type {}", response.type())
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
