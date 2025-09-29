package com.mediator_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final RoomManager roomManager;

    private final SessionManager sessionManager;

    public Map<String, Object> getRooms() {
        return new HashMap<>() {{
            put("activeRooms", roomManager.getActiveRooms());
        }};
    }

    public Map<String, Object> getSessions() {
        return new HashMap<>() {{
            put("totalSessions", sessionManager.getAll().size());
            put("activeUsers", sessionManager.getAll().keySet());
        }};
    }

    public List<String> getUsersInRoom(String roomId) {
        return roomManager.getUsersId(roomId).stream().toList();
    }
}
