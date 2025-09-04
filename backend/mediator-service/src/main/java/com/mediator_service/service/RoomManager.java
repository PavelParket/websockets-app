package com.mediator_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediator_service.dto.MessageResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomManager {

    @Getter
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    private final Map<String, Deque<MessageResponse>> histories = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        log.info("Session sessionId={} joined room roomId={}", session.getId(), roomId);

        Deque<MessageResponse> history = histories.get(roomId);

        if (history != null && !history.isEmpty()) {
            try {
                MessageResponse messageResponse = new MessageResponse("history", "system", null, roomId, mapper.writeValueAsString(history));
                session.sendMessage(new TextMessage(mapper.writeValueAsString(messageResponse)));
            } catch (Exception e) {
                log.error("Failed to send history to session {}", session.getId(), e);
            }
        }
    }

    public void removeSession(String roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = rooms.get(roomId);

        if (sessions != null) {
            sessions.remove(session);

            if (sessions.isEmpty()) {
                rooms.remove(roomId, sessions);
            }
        }

        log.info("Session {} left room {}", session.getId(), roomId);
    }

    public void broadcast(String roomId, MessageResponse messageResponse, WebSocketSession sender) {
        try {
            String json = mapper.writeValueAsString(messageResponse);

            Set<WebSocketSession> sessions = rooms.get(roomId);

            if (sessions == null || sessions.isEmpty()) return;

            for (WebSocketSession session : sessions) {
                if (session.equals(sender)) {
                    continue;
                }

                try {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(json));
                    }
                } catch (IOException e) {
                    log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                }
            }

            if ("message".equals(messageResponse.type())) {
                saveMessage(roomId, messageResponse);
            }

            log.info("Broadcast in room {} from {} → {} recipients", roomId, messageResponse.fromUserId(), sessions.size() - 1);
        } catch (Exception e) {
            log.error("Failed to broadcast message", e);
        }
    }

    public Set<String> getActiveRooms() {
        return rooms.keySet();
    }

    public void saveMessage(String roomId, MessageResponse messageResponse) {
        histories.computeIfAbsent(roomId, k -> new ArrayDeque<>());

        Deque<MessageResponse> history = histories.get(roomId);

        history.addLast(messageResponse);
    }
}

