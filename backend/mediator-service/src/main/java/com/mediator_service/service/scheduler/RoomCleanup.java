package com.mediator_service.service.scheduler;

import com.mediator_service.service.manager.ChatRoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomCleanup {

    private final ChatRoomManager chatRoomManager;

    public void cleanup() {
        int removedFromRooms = 0;

        for (Map.Entry<String, Set<WebSocketSession>> entry : chatRoomManager.getRooms().entrySet()) {
            String roomId = entry.getKey();
            Set<WebSocketSession> sessions = entry.getValue();

            sessions.removeIf(session -> !session.isOpen());

            if (sessions.isEmpty()) {
                chatRoomManager.getRooms().remove(roomId, sessions);
                removedFromRooms++;
            }
        }

        if (removedFromRooms > 0) {
            log.info("RoomCleanup removed {} empty rooms", removedFromRooms);
        }
    }
}
