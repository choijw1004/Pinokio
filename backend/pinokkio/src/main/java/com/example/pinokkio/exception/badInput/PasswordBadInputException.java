package com.example.pinokkio.exception.badInput;

import com.example.pinokkio.exception.base.BadInputException;

import java.util.Map;

public class PasswordBadInputException extends BadInputException {
    public PasswordBadInputException(String password) {
        super("PASSWORD_01", "두 비밀번호가 같지 않습니다.", Map.of("password", password));
    }
}
