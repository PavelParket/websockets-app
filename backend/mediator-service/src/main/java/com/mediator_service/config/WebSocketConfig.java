package com.mediator_service.config;

import com.mediator_service.interceptor.UserHandshakeInterceptor;
import com.mediator_service.message.ws_handler.ChatWebSocketHandler;
import com.mediator_service.message.ws_handler.GameWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatHandler;

    private final GameWebSocketHandler gameHandler;

    private final UserHandshakeInterceptor userHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/ws/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(userHandshakeInterceptor);

        registry.addHandler(gameHandler, "/ws/game")
                .setAllowedOriginPatterns("*")
                .addInterceptors(userHandshakeInterceptor);
    }
}
