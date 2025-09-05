package com.mediator_service.mapper;

import com.mediator_service.domain.dto.MessageRequest;
import com.mediator_service.domain.dto.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.socket.WebSocketSession;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "fromUserId", source = "session", qualifiedByName = "extractUserId")
    @Mapping(target = "roomId", source = "session", qualifiedByName = "extractRoomId")
    MessageResponse toResponse(MessageRequest request, WebSocketSession session);

    @Named("extractUserId")
    default String extractUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    @Named("extractRoomId")
    default String extractRoomId(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }
}
