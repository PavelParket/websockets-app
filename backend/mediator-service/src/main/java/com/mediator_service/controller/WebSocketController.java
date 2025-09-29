package com.mediator_service.controller;

import com.mediator_service.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ws")
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService service;

    @GetMapping("/monitor/rooms")
    public ResponseEntity<Map<String, Object>> getRooms() {
        return new ResponseEntity<>(service.getRooms(), HttpStatus.OK);
    }

    @GetMapping("/monitor/sessions")
    public ResponseEntity<Map<String, Object>> getSessions() {
        return new ResponseEntity<>(service.getSessions(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> getUsersInRoom(@RequestParam String roomId) {
        return new ResponseEntity<>(service.getUsersInRoom(roomId), HttpStatus.OK);
    }
}
