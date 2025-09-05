package com.mediator_service.service;

import com.mediator_service.strategy.SessionRemovalPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Qualifier("closeOnRemovePolicy")
    private final SessionRemovalPolicy removalPolicy;

    public void register(String userId, WebSocketSession session) {
        if (userId == null || session == null) {
            return;
        }

        WebSocketSession old = sessions.put(userId, session);

        if (old != null && old.isOpen()) {
            removalPolicy.onRemove(userId, old);
        }

        log.info("User {} registered session {}", userId, session.getId());
    }

    public void remove(String userId) {
        WebSocketSession session = sessions.remove(userId);

        if (session != null && session.isOpen()) {
            removalPolicy.onRemove(userId, session);
        }

        log.info("User {} removed session {}", userId, session != null ? session.getId() : null);
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
