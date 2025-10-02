package com.mediator_service.message.serializer;

import com.mediator_service.domain.dto.RoomMessage;

public interface MessageSerializer {

    String serialize(RoomMessage message) throws Exception;
}
