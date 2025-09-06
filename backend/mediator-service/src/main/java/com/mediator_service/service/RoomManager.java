package com.mediator_service.service;

import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.message.history.MessageHistoryService;
import com.mediator_service.message.serializer.MessageSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomManager {

    @Getter
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    private final MessageHistoryService service;

    @Qualifier("jsonSerializer")
    private final MessageSerializer serializer;

    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        log.info("Session \"{}\" joined room \"{}\"", session.getId(), roomId);

        var history = service.getHistory(roomId);

        if (!history.isEmpty()) {
            try {
                MessageResponse messageResponse = new MessageResponse(
                        "history",
                        "system",
                        (String) session.getAttributes().get("userId"),
                        roomId,
                        history.toString()
                );
                session.sendMessage(new TextMessage(serializer.serialize(messageResponse)));
            } catch (Exception e) {
                log.error("Failed to send history to session {}", session.getId(), e);
            }
        }
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

    public void broadcast(String roomId, MessageResponse message, WebSocketSession sender) {
        try {
            String json = serializer.serialize(message);

            var sessions = rooms.get(roomId);

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

            if ("message".equals(message.type())) {
                service.save(roomId, message);
            }

            log.info("Broadcast in room \"{}\" from {} → {} recipients", roomId, message.fromUserId(), sessions.size());
        } catch (Exception e) {
            log.error("Failed to broadcast message", e);
        }
    }

    public Set<String> getActiveRooms() {
        return rooms.keySet();
    }
}
