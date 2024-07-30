package com.example.pinokkio.exception;

import com.example.pinokkio.exception.base.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class RoomNotAvailableException extends NotFoundException {
    public RoomNotAvailableException(UUID roomId) {
        super("ROOM_01", "해당 방에 접근할 수 없습니다.", Map.of("roomId", String.valueOf(roomId)));
    }
}