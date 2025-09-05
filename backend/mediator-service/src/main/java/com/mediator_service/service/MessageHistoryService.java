package com.mediator_service.service;

import com.mediator_service.dto.MessageResponse;

import java.util.Deque;

public interface MessageHistoryService {

    void save(String roomId, MessageResponse messageResponse);

    Deque<MessageResponse> getHistory(String roomId);
}
