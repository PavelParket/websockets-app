package com.mediator_service.strategy;

import org.springframework.web.socket.WebSocketSession;

public interface SessionRemovalPolicy {

    void onRemove(String userId, WebSocketSession session);
}
