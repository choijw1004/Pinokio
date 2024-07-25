package com.example.pinokkio.exception.notFound;

import com.example.pinokkio.exception.base.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(UUID roomId) {
        super("ROOM_01", "아이디에 부합한 방을 찾을 수 없습니다.", Map.of("roomId", String.valueOf(roomId)));
    }
}