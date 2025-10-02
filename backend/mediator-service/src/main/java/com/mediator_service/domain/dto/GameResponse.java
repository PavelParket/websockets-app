package com.mediator_service.domain.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record GameResponse(
        String type,

        String fromUserId,

        String roomId,

        String player,

        Integer cell,

        List<String> board,

        Map<String, String> playersSymbols,

        String currentPlayer,

        String winner,

        String message
) implements RoomMessage {
}
