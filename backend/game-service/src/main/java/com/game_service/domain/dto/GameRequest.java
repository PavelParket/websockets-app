package com.game_service.domain.dto;

import lombok.Builder;

@Builder
public record GameRequest(
        String type,

        String player,

        Integer cell
) {
}
