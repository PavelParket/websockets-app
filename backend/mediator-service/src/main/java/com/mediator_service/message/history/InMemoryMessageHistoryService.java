package com.mediator_service.message.history;

import com.mediator_service.domain.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryMessageHistoryService implements MessageHistoryService {

    private final Map<String, Deque<MessageResponse>> histories = new ConcurrentHashMap<>();

    @Override
    public void save(String roomId, MessageResponse message) {
        histories.computeIfAbsent(roomId, k -> new ArrayDeque<>()).addLast(message);
    }

    @Override
    public Deque<MessageResponse> getHistory(String roomId) {
        return histories.getOrDefault(roomId, new ArrayDeque<>());
    }
}
