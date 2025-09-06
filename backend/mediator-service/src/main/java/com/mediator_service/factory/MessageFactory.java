package com.mediator_service.factory;

import com.mediator_service.domain.dto.MessageRequest;
import com.mediator_service.domain.dto.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.socket.WebSocketSession;

@Mapper(componentModel = "spring")
public interface MessageFactory {

    @Mapping(target = "fromUserId", source = "session", qualifiedByName = "extractUserId")
    @Mapping(target = "roomId", source = "session", qualifiedByName = "extractRoomId")
    MessageResponse toMessage(MessageRequest request, WebSocketSession session);

    @Mapping(target = "type", constant = "system")
    @Mapping(target = "fromUserId", constant = "system")
    @Mapping(target = "toUserId", constant = "all")
    MessageResponse toSystemMessage(String roomId, String content);

    @Named("extractUserId")
    default String extractUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    @Named("extractRoomId")
    default String extractRoomId(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }
}
