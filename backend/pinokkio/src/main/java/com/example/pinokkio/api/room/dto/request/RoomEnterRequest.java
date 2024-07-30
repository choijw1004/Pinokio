package com.example.pinokkio.api.room.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class RoomEnterRequest {
    private String roomId;
    private String userId;
}
