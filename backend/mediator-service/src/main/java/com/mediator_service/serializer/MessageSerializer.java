package com.mediator_service.serializer;

import com.mediator_service.dto.MessageResponse;

public interface MessageSerializer {

    String serialize(MessageResponse message) throws Exception;
}
