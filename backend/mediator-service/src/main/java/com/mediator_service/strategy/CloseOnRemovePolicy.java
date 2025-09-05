package com.mediator_service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service("closeOnRemovePolicy")
@Slf4j
public class CloseOnRemovePolicy implements SessionRemovalPolicy {

    @Override
    public void onRemove(String userId, WebSocketSession session) {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                log.warn("Failed to close session \"{}\" for user \"{}\"", session.getId(), userId, e);
            }
        }
    }
}
