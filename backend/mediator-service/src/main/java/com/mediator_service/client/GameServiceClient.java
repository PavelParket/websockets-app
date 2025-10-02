package com.mediator_service.client;

import com.mediator_service.domain.dto.GameRequest;
import com.mediator_service.domain.dto.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameServiceClient {

    private final RestTemplate restTemplate;

    @Value("${app.game.url}")
    private String url;

    public Optional<GameResponse> processMove(String roomId, String userId, GameRequest request) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(url)
                    .path("/game/move")
                    .queryParam("roomId", roomId)
                    .queryParam("userId", userId)
                    .build()
                    .toUri();

            ResponseEntity<GameResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    GameResponse.class
            );

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            log.error("Game service call failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<GameResponse> startGame(String roomId, Set<String> players) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(url)
                    .path("/game/start")
                    .queryParam("roomId", roomId)
                    .build()
                    .toUri();

            Map<String, Object> body = Map.of("players", players);

            ResponseEntity<GameResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    new HttpEntity<>(body),
                    GameResponse.class
            );

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            log.error("Game service start failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
