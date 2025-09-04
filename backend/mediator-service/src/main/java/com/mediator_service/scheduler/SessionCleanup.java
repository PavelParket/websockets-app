package com.mediator_service.scheduler;

import com.mediator_service.service.RoomManager;
import com.mediator_service.service.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCleanup {

    private final RoomManager roomManager;

    private final SessionManager sessionManager;

    @Scheduled(fixedDelay = 60000)
    public void cleanup() {
        int removedSessions = 0;

        for (String userId : new HashSet<>(sessionManager.getAll().keySet())) {
            if (!sessionManager.isActive(userId)) {
                sessionManager.remove(userId);
                removedSessions++;
            }
        }

        int removedFromRooms = 0;

        for (Map.Entry<String, Set<WebSocketSession>> entry : roomManager.getRooms().entrySet()) {
            String roomId = entry.getKey();
            Set<WebSocketSession> sessions = entry.getValue();

            for (Iterator<WebSocketSession> iterator = sessions.iterator(); iterator.hasNext(); ) {
                WebSocketSession session = iterator.next();

                if (!session.isOpen()) {
                    iterator.remove();
                    removedFromRooms++;
                }
            }

            if (sessions.isEmpty()) {
                roomManager.getRooms().remove(roomId, sessions);
            }
        }

        if (removedSessions > 0 || removedFromRooms > 0) {
            log.info("Cleanup removed {} inactive user sessions and {} closed room sessions", removedSessions, removedFromRooms);
        }
    }
}
