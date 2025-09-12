package com.notification_service.config;

import com.notification_service.event.RoomEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, RoomEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.GROUP_ID_CONFIG, "notification-service",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                        JsonDeserializer.TRUSTED_PACKAGES, "*",
                        JsonDeserializer.VALUE_DEFAULT_TYPE, "com.notification_service.event.RoomEvent",
                        JsonDeserializer.USE_TYPE_INFO_HEADERS, false
                )
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RoomEvent> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>() {{
            setConsumerFactory(consumerFactory());
        }};
    }
}
