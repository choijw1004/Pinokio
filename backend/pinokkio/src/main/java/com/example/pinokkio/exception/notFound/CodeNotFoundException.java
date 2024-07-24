package com.example.pinokkio.exception.notFound;

import com.example.pinokkio.exception.base.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class CodeNotFoundException extends NotFoundException {
    public CodeNotFoundException(UUID codeId) {
        super("CODE_01", "아이디에 부합한 카테고리를 찾을 수 없습니다.", Map.of("codeId", String.valueOf(codeId)));
    }
}
