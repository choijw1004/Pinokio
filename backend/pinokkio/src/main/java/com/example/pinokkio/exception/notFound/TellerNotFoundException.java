package com.example.pinokkio.exception.notFound;

import com.example.pinokkio.exception.base.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class TellerNotFoundException extends NotFoundException {
    public TellerNotFoundException(UUID tellerId) {
        super("TELLER_01", "아이디에 부합한 상담원을 찾을 수 없습니다.", Map.of("tellerId", String.valueOf(tellerId)));
    }
}