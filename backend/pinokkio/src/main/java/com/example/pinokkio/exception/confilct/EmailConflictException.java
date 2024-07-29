package com.example.pinokkio.exception.confilct;

import com.example.pinokkio.exception.base.ConflictException;

import java.util.Map;

public class EmailConflictException extends ConflictException {
    public EmailConflictException(String email) {
        super("EMAIL_01", "중복된 이메일입니다.", Map.of("email", email));
    }
}
