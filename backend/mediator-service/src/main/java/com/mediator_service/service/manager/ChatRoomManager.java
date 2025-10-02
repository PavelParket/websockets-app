package com.mediator_service.service.manager;

import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.message.history.MessageHistoryService;
import com.mediator_service.message.serializer.MessageSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@Slf4j
public class ChatRoomManager extends AbstractRoomManager {

    private final MessageHistoryService historyService;

    public ChatRoomManager(MessageSerializer serializer, MessageHistoryService historyService) {
        super(serializer);
        this.historyService = historyService;
    }

    @Override
    protected void onAddSession(String roomId, WebSocketSession session) {
        var history = historyService.getHistory(roomId);

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

    @Override
    public String getName() {
        return "chatRooms";
    }
}
