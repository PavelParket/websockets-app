package com.mediator_service.provider;

import org.springframework.http.server.ServerHttpRequest;

public interface UserIdentityProvider {

    String resolveUserId(ServerHttpRequest request);

    String resolveRoomId(ServerHttpRequest request);
}
