package com.mediator_service.message.handler;

import com.mediator_service.domain.dto.MessageResponse;
import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {

    boolean supports(String type);

    void handle(MessageResponse message, WebSocketSession session);
}
