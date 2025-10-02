package com.mediator_service.message.ws_handler;

import com.mediator_service.domain.dto.GameRequest;
import com.mediator_service.domain.dto.GameResponse;
import com.mediator_service.message.serializer.JsonDeserializer;
import com.mediator_service.service.manager.GameRoomManager;
import com.mediator_service.service.manager.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class GameWebSocketHandler extends AppWebSocketHandler<GameRoomManager> {

    private final JsonDeserializer jsonDeserializer;

    public GameWebSocketHandler(SessionManager sessionManager, GameRoomManager gameManager, JsonDeserializer jsonDeserializer) {
        super(sessionManager, gameManager);
        this.jsonDeserializer = jsonDeserializer;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            GameRequest request = jsonDeserializer.deserialize(message.getPayload(), GameRequest.class);


            String roomId = getRoomIdFromSession(session);
            String userId = getUserIdFromSession(session);

            switch (request.type()) {
                case "move" -> roomManager.processMove(roomId, userId, request, session);
                case "ready" -> roomManager.processReady(roomId, userId, session);
                case "join" -> roomManager.handleJoin(roomId, userId, session);
                case "leave" -> roomManager.handleLeave(roomId, userId, session);
                default -> {
                    log.warn("Unknown game request type: {}", request.type());
                    roomManager.sendMessage(session, GameResponse.builder()
                            .type("system")
                            .fromUserId("system")
                            .roomId(roomId)
                            .message("Unknown request type: " + request.type())
                            .build());
                }
            }
        } catch (Exception e) {
            log.warn("Invalid JSON from session {}: {}", session.getId(), e.getMessage());
            roomManager.sendMessage(session, GameResponse.builder()
                    .type("system")
                    .fromUserId("system")
                    .roomId(getRoomIdFromSession(session))
                    .message("Invalid message format")
                    .build());
        }
    }

    @Override
    protected void onJoin(String roomId, String userId, WebSocketSession session) {
    }

    @Override
    protected void onLeave(String roomId, String userId, WebSocketSession session) {
    }
}
