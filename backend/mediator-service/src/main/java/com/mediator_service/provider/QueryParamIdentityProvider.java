package com.mediator_service.provider;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component("queryParamIdentityProvider")
public class QueryParamIdentityProvider implements UserIdentityProvider {

    @Override
    public String resolveUserId(ServerHttpRequest request) {
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String userId = params.getFirst("userId");

        return (userId == null || userId.isBlank())
                ? "guest-" + System.currentTimeMillis()
                : userId;
    }

    @Override
    public String resolveRoomId(ServerHttpRequest request) {
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String roomId = params.getFirst("roomId");

        return (roomId == null || roomId.isBlank())
                ? "default"
                : roomId;
    }
}
