package com.game_service.exception;

import com.game_service.domain.dto.GameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<GameResponse> handleGameException(GameException ex) {
        log.warn("Game error in room {} for user {}: {}", ex.getRoomId(), ex.getUserId(), ex.getMessage());

        return new ResponseEntity<>(
                GameResponse.builder()
                        .type("system")
                        .roomId(ex.getRoomId())
                        .fromUserId(ex.getUserId())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
