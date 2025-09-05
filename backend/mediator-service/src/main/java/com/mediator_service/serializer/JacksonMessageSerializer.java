package com.mediator_service.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediator_service.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("jsonSerializer")
@RequiredArgsConstructor
public class JacksonMessageSerializer implements MessageSerializer {

    private final ObjectMapper mapper;

    @Override
    public String serialize(MessageResponse message) throws Exception {
        return mapper.writeValueAsString(message);
    }
}
