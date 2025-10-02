package com.mediator_service.service.manager;

import com.mediator_service.client.GameServiceClient;
import com.mediator_service.domain.dto.GameRequest;
import com.mediator_service.domain.dto.GameResponse;
import com.mediator_service.domain.dto.RoomMessage;
import com.mediator_service.message.serializer.MessageSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class GameRoomManager extends AbstractRoomManager {

    private final Map<String, Set<String>> readyPlayers = new ConcurrentHashMap<>();

    private final Set<String> startInProgress = ConcurrentHashMap.newKeySet();

    private final GameServiceClient gameClient;

    private final SessionManager sessionManager;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public GameRoomManager(MessageSerializer serializer, GameServiceClient gameClient, SessionManager sessionManager) {
        super(serializer);
        this.gameClient = gameClient;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void onAddSession(String roomId, WebSocketSession session) {
    }

    // === MOVE ===
    public void processMove(String roomId, String userId, GameRequest request, WebSocketSession session) {
        if (!getUsersId(roomId).contains(userId)) {
            sendToSession(session, GameResponse.builder()
                    .type("system")
                    .fromUserId("system")
                    .roomId(roomId)
                    .message("You are not participant of this room")
                    .build());
            return;
        }

        try {
            Optional<GameResponse> response = gameClient.processMove(roomId, userId, request);

            if (response.isPresent()) {
                broadcast(roomId, response.get(), session);
            } else {
                sendToSession(session, GameResponse.builder()
                        .type("system")
                        .fromUserId("system")
                        .roomId(roomId)
                        .message("Game service unavailable")
                        .build());
            }
        } catch (Exception e) {
            log.error("Failed to process move via game-service", e);

            sendToSession(session, GameResponse.builder()
                    .type("system")
                    .fromUserId("system")
                    .roomId(roomId)
                    .message("Internal error")
                    .build());
        }
    }

    // === READY ===
    public void processReady(String roomId, String userId, WebSocketSession session) {
        var users = getUsersId(roomId);

        log.error("Users {}, {}, {}", users, userId, roomId);

        if (!users.contains(userId)) {
            sendToSession(session, GameResponse.builder()
                    .type("system")
                    .fromUserId("system")
                    .roomId(roomId)
                    .message("You are not participant of this room")
                    .build());

            return;
        }

        readyPlayers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);

        log.info("Player \"{}\" ready in \"{}\"", userId, roomId);

        broadcast(roomId, GameResponse.builder()
                .type("ready")
                .fromUserId(userId)
                .roomId(roomId)
                .message("User \"" + userId + "\" is ready")
                .build(), null);

        if (allPlayersReady(roomId) && startInProgress.add(roomId)) {
            executor.submit(() -> {
                try {
                    Set<String> players = new HashSet<>(getUsersId(roomId));
                    Optional<GameResponse> response = gameClient.startGame(roomId, players);

                    if (response.isPresent()) {
                        broadcastStartGame(roomId, response.get());
                    } else {
                        broadcast(roomId, GameResponse.builder()
                                .type("system")
                                .roomId(roomId)
                                .message("Failed to start game")
                                .build(), null);
                    }

                    readyPlayers.remove(roomId);
                } finally {
                    startInProgress.remove(roomId);
                }
            });
        }
    }

    // === JOIN ===
    public void handleJoin(String roomId, String userId, WebSocketSession session) {
        log.info("User \"{}\" joined room \"{}\"", userId, roomId);

        GameResponse response = GameResponse.builder()
                .type("join")
                .fromUserId(userId)
                .roomId(roomId)
                .message("User \"" + userId + "\" joined the room")
                .build();

        broadcast(roomId, response, session);
    }

    // === LEAVE ===
    public void handleLeave(String roomId, String userId, WebSocketSession session) {
        log.info("User \"{}\" left room \"{}\"", userId, roomId);

        readyPlayers.getOrDefault(roomId, ConcurrentHashMap.newKeySet()).remove(userId);

        GameResponse response = GameResponse.builder()
                .type("leave")
                .fromUserId(userId)
                .roomId(roomId)
                .message("User \"" + userId + "\" left the room")
                .build();

        broadcast(roomId, response, session);
    }

    private boolean allPlayersReady(String roomId) {
        Set<String> players = getUsersId(roomId);
        return !players.isEmpty()
                && readyPlayers.containsKey(roomId)
                && readyPlayers.get(roomId).containsAll(players);
    }

    public void sendMessage(WebSocketSession session, RoomMessage message) {
        sendToSession(session, message);
    }

    private void broadcastStartGame(String roomId, GameResponse response) {
        Map<String, String> playersSymbols = response.playersSymbols();

        if (playersSymbols == null || playersSymbols.isEmpty()) {
            broadcast(roomId, response, null);
            return;
        }

        for (String userId : playersSymbols.keySet()) {
            String symbol = playersSymbols.get(userId);

            GameResponse personalized = GameResponse.builder()
                    .type(response.type())
                    .fromUserId(response.fromUserId())
                    .roomId(response.roomId())
                    .player(symbol)
                    .board(response.board())
                    .playersSymbols(null)
                    .currentPlayer(response.currentPlayer())
                    .winner(response.winner())
                    .message(response.message())
                    .build();

            WebSocketSession session = sessionManager.getByUserId(userId);

            if (session != null) {
                sendToSession(session, personalized);
            }
        }
    }

    @Override
    public String getName() {
        return "gameRooms";
    }
}
