package com.mediator_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomManager {

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    public void removeSession(String roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = rooms.get(roomId);

        if (sessions != null) {
            sessions.remove(session);

            if (sessions.isEmpty()) {
                rooms.remove(roomId, sessions);
            }
        }
    }

    public void broadcast(String roomId, TextMessage message, WebSocketSession sender) {
        if (roomId == null || message == null) return;

        Set<WebSocketSession> sessions = rooms.get(roomId);

        if (sessions == null || sessions.isEmpty()) return;

        for (var iterator = sessions.iterator(); iterator.hasNext(); ) {
            WebSocketSession session = iterator.next();

            if (session.equals(sender)) {
                continue;
            }

            if (!session.isOpen()) {
                iterator.remove();
                continue;
            }

            try {
                synchronized (session) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                iterator.remove();
            }
        }

        if (sessions.isEmpty()) {
            rooms.remove(roomId, sessions);
        }
    }

    public Set<String> getActiveRooms() {
        return rooms.keySet();
    }
}
