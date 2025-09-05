package com.mediator_service.message.history;

import com.mediator_service.domain.dto.MessageResponse;

import java.util.Deque;

public interface MessageHistoryService {

    void save(String roomId, MessageResponse messageResponse);

    Deque<MessageResponse> getHistory(String roomId);
}
