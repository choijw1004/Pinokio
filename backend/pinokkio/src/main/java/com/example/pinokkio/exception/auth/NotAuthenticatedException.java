package com.example.pinokkio.exception.auth;

import com.example.pinokkio.exception.base.AuthenticationException;

public class NotAuthenticatedException extends AuthenticationException {
    public NotAuthenticatedException() {
        super("AUTH_01", "인증되지 않은 유저입니다.");
    }
}
