package com.mediator_service.message.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonDeserializer implements MessageDeserializer {

    private final ObjectMapper mapper;

    @Override
    public <T> T deserialize(String payload, Class<T> clazz) {
        try {
            return mapper.readValue(payload, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize JSON", e);
        }
    }
}
