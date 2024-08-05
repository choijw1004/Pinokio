package com.example.pinokkio.exception.domain.room;

import com.example.pinokkio.exception.base.NotFoundException;

public class RoomAccessRestrictedException extends NotFoundException {
    public RoomAccessRestrictedException() {
        super("NOT_FOUND_ROOM_02", "현재 입장 가능한 방이 없습니다.");
    }

}
