package com.mediator_service.message.ws_handler;

import com.mediator_service.domain.dto.MessageRequest;
import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.domain.enums.EventType;
import com.mediator_service.domain.event.RoomEvent;
import com.mediator_service.factory.MessageFactory;
import com.mediator_service.message.handler.MessageHandler;
import com.mediator_service.message.serializer.JsonDeserializer;
import com.mediator_service.service.RoomEventProducer;
import com.mediator_service.service.SystemMessageService;
import com.mediator_service.service.manager.ChatRoomManager;
import com.mediator_service.service.manager.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChatWebSocketHandler extends AppWebSocketHandler<ChatRoomManager> {

    private final List<MessageHandler> handlers;

    private final SystemMessageService systemMessageService;

    private final MessageFactory factory;

    private final JsonDeserializer jsonDeserializer;

    private final RoomEventProducer producer;

    public ChatWebSocketHandler(SessionManager sessionManager, ChatRoomManager chatManager, List<MessageHandler> handlers, SystemMessageService systemMessageService, MessageFactory factory, JsonDeserializer jsonDeserializer, RoomEventProducer producer) {
        super(sessionManager, chatManager);
        this.handlers = handlers;
        this.systemMessageService = systemMessageService;
        this.factory = factory;
        this.jsonDeserializer = jsonDeserializer;
        this.producer = producer;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            MessageRequest request = jsonDeserializer.deserialize(message.getPayload(), MessageRequest.class);
            MessageResponse response = factory.toMessage(request, session);

            handlers.stream()
                    .filter(messageHandler -> messageHandler.supports(response.type()))
                    .findFirst()
                    .ifPresentOrElse(
                            messageHandler -> {
                                messageHandler.handle(response, session);

                                RoomEvent event = new RoomEvent(EventType.MESSAGE_SENT, response.roomId(), response.fromUserId(), new ArrayList<>(roomManager.getUsersId(response.roomId())));
                                producer.send(event);
                            },
                            () -> log.warn("No handler found for type {}", response.type())
                    );
        } catch (Exception e) {
            log.error("Failed to handle message", e);
        }
    }


    @Override
    protected void onJoin(String roomId, String userId, WebSocketSession session) {
        systemMessageService.userJoined(roomId, userId);
    }

    @Override
    protected void onLeave(String roomId, String userId, WebSocketSession session) {
        systemMessageService.userLeft(roomId, userId);
    }
}
