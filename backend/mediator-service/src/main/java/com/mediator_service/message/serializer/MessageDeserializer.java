package com.mediator_service.message.serializer;

public interface MessageDeserializer {

    <T> T deserialize(String payload, Class<T> clazz);
}
