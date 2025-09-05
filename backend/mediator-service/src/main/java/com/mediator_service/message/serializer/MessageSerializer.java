package com.mediator_service.message.serializer;

import com.mediator_service.domain.dto.MessageResponse;

public interface MessageSerializer {

    String serialize(MessageResponse message) throws Exception;
}
