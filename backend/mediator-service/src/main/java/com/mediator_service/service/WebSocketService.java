package com.mediator_service.service;

import com.mediator_service.service.manager.AbstractRoomManager;
import com.mediator_service.service.manager.ChatRoomManager;
import com.mediator_service.service.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final List<AbstractRoomManager> roomManagers;

    private final SessionManager sessionManager;

    public Map<String, Object> getRooms() {
        return roomManagers.stream()
                .collect(Collectors.toMap(
                        AbstractRoomManager::getName,
                        AbstractRoomManager::getActiveRooms
                ));
    }

    public Map<String, Object> getSessions() {
        return new HashMap<>() {{
            put("totalSessions", sessionManager.getAll().size());
            put("activeUsers", sessionManager.getAll().keySet());
        }};
    }

    public List<String> getUsersInRoom(String roomId) {
        return roomManagers.stream()
                .filter(m -> m instanceof ChatRoomManager)
                .findFirst()
                .map(m -> m.getUsersId(roomId).stream().toList())
                .orElse(List.of());
    }
}
