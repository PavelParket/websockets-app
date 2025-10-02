package com.game_service.exception;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {
    private final String roomId;
    private final String userId;

    public GameException(String roomId, String userId, String message) {
        super(message);
        this.roomId = roomId;
        this.userId = userId;
    }
}

