package com.mediator_service.handler;

import com.mediator_service.dto.MessageResponse;
import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {

    boolean supports(String type);

    void handle(MessageResponse message, WebSocketSession session);
}
