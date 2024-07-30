package com.example.pinokkio.exception.notFound;

import com.example.pinokkio.exception.base.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class KioskNotFoundException extends NotFoundException {
    public KioskNotFoundException(UUID kioskId) {
        super("KIOSK_01", "아이디에 부합한 상품을 찾을 수 없습니다.", Map.of("kioskId", String.valueOf(kioskId)));
    }
}