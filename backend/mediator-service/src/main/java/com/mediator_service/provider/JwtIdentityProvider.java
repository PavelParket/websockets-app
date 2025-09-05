package com.mediator_service.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component("jwtIdentityProvider")
public class JwtIdentityProvider implements UserIdentityProvider {

    @Override
    public String resolveUserId(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get("Authorization");

        if (headers == null || headers.isEmpty()) {
            return "guest-" + System.currentTimeMillis();
        }

        String token = headers.getFirst().replace("Bearer ", "").trim();

        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);

            return node.has("sub") ? node.get("sub").asText() : "guest-" + System.currentTimeMillis();
        } catch (Exception e) {
            return "guest-" + System.currentTimeMillis();
        }
    }

    @Override
    public String resolveRoomId(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get("Authorization");

        if (headers == null || headers.isEmpty()) {
            return "default";
        }

        String token = headers.getFirst().replace("Bearer ", "").trim();

        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);

            return node.has("room") ? node.get("room").asText() : "default";
        } catch (Exception e) {
            return "default";
        }
    }
}
