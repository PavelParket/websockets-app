package com.game_service.service;

import com.game_service.domain.dto.GameRequest;
import com.game_service.domain.dto.GameResponse;
import com.game_service.exception.GameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final Map<String, List<String>> boards = new ConcurrentHashMap<>();

    private final Map<String, String> currentPlayers = new ConcurrentHashMap<>();

    private final Map<String, Map<String, String>> playerSymbols = new ConcurrentHashMap<>();

    private final Random random = new Random();

    public GameResponse startGame(String roomId, Map<String, Set<String>> body) {
        List<String> board = new ArrayList<>(Collections.nCopies(9, null));

        Set<String> players = body.get("players");

        if (players.size() != 2) {
            throw new IllegalArgumentException("Exactly two players required");
        }

        boards.put(roomId, board);

        List<String> playerList = new ArrayList<>(players);
        Collections.shuffle(playerList, random);
        String firstPlayerId = playerList.get(0);
        String secondPlayerId = playerList.get(1);

        Map<String, String> symbols = Map.of(
                firstPlayerId, "X",
                secondPlayerId, "O"
        );
        playerSymbols.put(roomId, symbols);

        String firstTurnPlayer = symbols.entrySet().stream()
                .filter(e -> e.getValue().equals("X"))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();

        currentPlayers.put(roomId, firstTurnPlayer);

        return GameResponse.builder()
                .type("start")
                .board(new ArrayList<>(board))
                .currentPlayer("X")
                .playersSymbols(new HashMap<>(symbols))
                .message("Game started: " + firstPlayerId + " is X, " + secondPlayerId + " is O")
                .build();
    }

    public GameResponse processMove(String roomId, String userId, GameRequest request) {
        List<String> board = boards.get(roomId);

        if (board == null) {
            throw new GameException(roomId, userId, "Game not found");
        }

        if (!Objects.equals(currentPlayers.get(roomId), userId)) {
            throw new GameException(roomId, userId, "Not your turn");
        }

        int cell = request.cell();
        if (cell < 0 || cell >= 9 || board.get(cell) != null) {
            throw new GameException(roomId, userId, "Invalid move");
        }

        String symbol = playerSymbols.get(roomId).get(userId);
        if (symbol == null || !symbol.equals(request.player())) {
            throw new GameException(roomId, userId, "Invalid player symbol");
        }

        board.set(cell, symbol);

        String winner = checkWinner(board);
        String nextSymbol = null;
        String nextUser;

        if (winner == null) {
            if (board.stream().allMatch(Objects::nonNull)) {
                winner = "Draw";
            } else {
                String opposite = symbol.equals("X") ? "O" : "X";
                nextUser = playerSymbols.get(roomId).entrySet().stream()
                        .filter(e -> e.getValue().equals(opposite))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow();
                currentPlayers.put(roomId, nextUser);
                nextSymbol = opposite;
            }
        }

        return GameResponse.builder()
                .type("move")
                .roomId(roomId)
                .fromUserId(userId)
                .player(symbol)
                .cell(cell)
                .board(new ArrayList<>(board))
                .currentPlayer(nextSymbol)
                .winner(winner)
                .message(
                        winner != null
                                ? (winner.equals("Draw") ? "It's a draw!" : "Winner is " + winner)
                                : "Move processed"
                )
                .build();
    }

    private String checkWinner(List<String> board) {
        int[][] lines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : lines) {
            String a = board.get(line[0]);
            if (a != null && a.equals(board.get(line[1])) && a.equals(board.get(line[2]))) {
                return a;
            }
        }
        return null;
    }
}
