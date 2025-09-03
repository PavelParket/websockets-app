package com.mediator_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Value("${spring.session.max.per-user}")
    private int maxSessionsPerUser;

    public void register(String userId, WebSocketSession session) {
        if (userId == null || session == null) {
            return;
        }

        WebSocketSession old = sessions.put(userId, session);

        if (old != null && old.isOpen()) {
            try {
                old.close();
            } catch (IOException e) {
                log.error("Close failed for user {}", userId, e);
            }
        }
    }

    public void remove(String userId) {
        WebSocketSession session = sessions.remove(userId);

        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public Map<String, WebSocketSession> getAll() {
        return sessions;
    }

    public WebSocketSession getByUserId(String userId) {
        WebSocketSession session = sessions.get(userId);

        if (session != null && !session.isOpen()) {
            sessions.remove(userId);

            return null;
        }

        return session;
    }

    public boolean isActive(String userId) {
        WebSocketSession session = sessions.get(userId);

        return session != null && session.isOpen();
    }
}
