package com.mediator_service.service;

import com.mediator_service.domain.event.RoomEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomEventProducer {

    private final KafkaTemplate<String, RoomEvent> template;

    private static final String TOPIC = "room-events";

    public void send(RoomEvent event) {
        template.send(TOPIC, event)
                .thenApply(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Message sent: topic={}, partition={}, offset={}, message {}", metadata.topic(), metadata.partition(), metadata.offset(), event);
                    return metadata;
                })
                .exceptionally(e -> {
                    log.error("Failed to send message", e);
                    throw new RuntimeException(e);
                });
    }
}
