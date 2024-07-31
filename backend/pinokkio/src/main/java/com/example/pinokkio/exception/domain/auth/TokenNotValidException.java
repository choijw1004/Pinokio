package com.example.pinokkio.exception.domain.auth;

import com.example.pinokkio.exception.base.AuthorizationException;

/**
 * 403 UNAUTHORIZED
 */
public class TokenNotValidException extends AuthorizationException {
    public TokenNotValidException() {
        super("UNAUTHORIZED_TOKEN_02", "AccessToken 유효성 검사에 실패하였습니다.");
    }
}
