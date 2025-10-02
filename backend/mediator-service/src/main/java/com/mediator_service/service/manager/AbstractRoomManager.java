package com.mediator_service.service.manager;

import com.mediator_service.domain.dto.RoomMessage;
import com.mediator_service.message.serializer.MessageSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public abstract class AbstractRoomManager {

    @Getter
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Qualifier("jsonSerializer")
    protected final MessageSerializer serializer;

    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);

        onAddSession(roomId, session);

        log.info("Session \"{}\" joined room \"{}\"", session.getId(), roomId);
    }

    public void removeSession(String roomId, WebSocketSession session) {
        var sessions = rooms.get(roomId);

        if (sessions != null) {
            sessions.remove(session);

            if (sessions.isEmpty()) {
                rooms.remove(roomId, sessions);
            }
        }

        log.info("Session {} left room \"{}\"", session.getId(), roomId);
    }

    public void broadcast(String roomId, RoomMessage message, WebSocketSession sender) {
        try {
            String json = serializer.serialize(message);

            var sessions = rooms.get(roomId);

            if (sessions == null || sessions.isEmpty()) return;

            for (WebSocketSession session : sessions) {
                try {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(json));
                    }
                } catch (IOException e) {
                    log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                }
            }

            log.info("Broadcast in room \"{}\" from {} → {} recipients", roomId, message.fromUserId(), sessions.size());
        } catch (Exception e) {
            log.error("Failed to broadcast message", e);
        }
    }

    public Set<String> getActiveRooms() {
        return rooms.keySet();
    }

    public Set<String> getUsersId(String roomId) {
        return getRooms().getOrDefault(roomId, Set.of()).stream()
                .map(session -> (String) session.getAttributes().get("userId"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    protected abstract void onAddSession(String roomId, WebSocketSession session);

    protected void sendToSession(WebSocketSession session, RoomMessage message) {
        try {
            String json = serializer.serialize(message);

            if (session != null && session.isOpen()) {
                synchronized (session) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            log.warn("Failed to send private message to session \"{}\": {}", session != null ? session.getId() : "n/a", e.getMessage());
        }
    }

    public abstract String getName();
}
