package com.example.pinokkio.exception.domain.auth;

import com.example.pinokkio.exception.base.AuthorizationException;

/**
 * 403 UNAUTHORIZED
 */
public class ExpiredTokenException extends AuthorizationException {
    public ExpiredTokenException() {
        super("UNAUTHORIZED_TOKEN_01", "AccessToken 의 유효기간이 지났습니다. 토큰을 재발급 받아주세요.");
    }
}
