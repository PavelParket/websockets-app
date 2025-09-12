package com.notification_service.config;

import com.notification_service.handler.AppWebSocketHandler;
import com.notification_service.interceptor.UserHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${app.websocket.endpoint}")
    private String endpoint;

    private final AppWebSocketHandler handler;

    private final UserHandshakeInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, endpoint)
                .setAllowedOriginPatterns("*")
                .addInterceptors(interceptor);
    }
}
