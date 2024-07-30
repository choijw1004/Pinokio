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

    @Schema(description = "응답 메시지", example = "Room created successfully")
    private String message;

    @Schema(description = "응답 데이터(Livekit Access Token)", example = "{\n" +
            "    \"token\": \"eyJ0eXAiOiJKV1QiLCJhbG...\"\n" +
            "}")
    private Object data;
}
