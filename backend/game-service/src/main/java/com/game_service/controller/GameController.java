package com.game_service.controller;

import com.game_service.domain.dto.GameRequest;
import com.game_service.domain.dto.GameResponse;
import com.game_service.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService service;

    @PostMapping("/start")
    public ResponseEntity<GameResponse> startGame(@RequestParam String roomId, @RequestBody Map<String, Set<String>> body) {
        return new ResponseEntity<>(service.startGame(roomId, body), HttpStatus.OK);
    }

    @PostMapping("/move")
    public ResponseEntity<GameResponse> processMove(@RequestParam String roomId, @RequestParam String userId, @RequestBody GameRequest request) {
        return new ResponseEntity<>(service.processMove(roomId, userId, request), HttpStatus.OK);
    }
}
