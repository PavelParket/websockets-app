package com.mediator_service.provider;

import com.mediator_service.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component("jwtIdentityProvider")
@RequiredArgsConstructor
public class JwtIdentityProvider implements UserIdentityProvider {

    private final JwtProvider provider;

    @Override
    public String resolveUserId(ServerHttpRequest request) {
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String token = params.getFirst("token");

        if (token == null || token.isBlank()) {
            return "guest-" + System.currentTimeMillis();
        }

        if (!provider.validate(token)) {
            throw new JwtException("Invalid JWT token");
        }

        String username = provider.getUsername(token);

        return (username != null && !username.isBlank()) ? username : "guest-" + System.currentTimeMillis();
    }

    @Override
    public String resolveRoomId(ServerHttpRequest request) {
        var params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String roomId = params.getFirst("roomId");

        return (roomId == null || roomId.isBlank()) ? "default" : roomId;
    }
}
