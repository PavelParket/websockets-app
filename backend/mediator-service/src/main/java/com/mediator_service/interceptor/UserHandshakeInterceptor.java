package com.mediator_service.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        var queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();

        String userId = queryParams.getFirst("userId");

        if (userId == null || userId.isBlank()) {
            userId = "guest-" + System.currentTimeMillis();
        }

        attributes.put("userId", userId);

        String roomId = queryParams.getFirst("roomId");

        if (roomId == null || roomId.isBlank()) {
            roomId = "default";
        }

        attributes.put("roomId", roomId);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
