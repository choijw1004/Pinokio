package com.example.pinokkio.api.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeetingResponse {
    private String message;
    private Object data;
}