package com.example.pinokkio.exception.notFound;

import com.example.pinokkio.exception.base.NotFoundException;
import java.util.Map;

public class PosNotFoundException extends NotFoundException {
    public PosNotFoundException(String posId) {
        super("POS_01", "아이디에 부합한 포스를 찾을 수 없습니다.", Map.of("posId", posId));
    }
}
