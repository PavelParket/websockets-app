package com.mediator_service.message.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediator_service.domain.dto.MessageResponse;
import com.mediator_service.domain.dto.RoomMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("jsonSerializer")
@RequiredArgsConstructor
public class JacksonMessageSerializer implements MessageSerializer {

    private final ObjectMapper mapper;

    @Override
    public String serialize(RoomMessage message) throws Exception {
        return mapper.writeValueAsString(message);
    }
}
