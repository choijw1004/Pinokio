package com.example.pinokkio.api.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "화상 상담 응답 DTO")
public class RoomResponse {

    @Schema(description = "Room UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String roomId;

    @Schema(description = "Livekit Access Token", example = "{\n" +
            "    \"token\": \"eyJ0eXAiOiJKV1QiLCJhbG...\"\n" +
            "}")
    private String token;
}
