package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.KioskAuthRequest;
import com.example.pinokkio.api.auth.dto.request.PosAuthRequest;
import com.example.pinokkio.api.auth.dto.request.TellerAuthRequest;
import com.example.pinokkio.config.RedisUtil;
import com.example.pinokkio.config.jwt.JwtTokenProvider;
import com.example.pinokkio.exception.base.AuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
    @Service
    @RequiredArgsConstructor
    public class AuthService {


        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final RedisUtil redisUtil;


        @Transactional
        public AuthToken authenticatePos(PosAuthRequest posAuthRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            posAuthRequest.getUsername(),
                            posAuthRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_POS", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "POS_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        }
        catch (AuthenticationException e) {
            throw new AuthenticationException("POS 인증 실패", e.getMessage());
        }
    }


    @Transactional
    public AuthToken authenticateKiosk(KioskAuthRequest kioskAuthRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            kioskAuthRequest.getUsername(),
                            kioskAuthRequest.getPassword()
                    );

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_KIOSK", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "KIOSK_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        }
        catch (AuthenticationException e) {
            throw new AuthenticationException("KIOSK 인증 실패", e.getMessage());
        }
    }


    /**
     * Teller 인증 기반 토큰 return
     */
    @Transactional
    public AuthToken authenticateTeller(TellerAuthRequest tellerAuthRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            tellerAuthRequest.getUsername(),
                            tellerAuthRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_TELLER", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "TELLER_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        }
        catch (AuthenticationException e) {
            throw new AuthenticationException("TELLER 인증 실패", e.getMessage());
        }
    }


    private void saveRefreshTokenToRedis(String username, String role, String accessToken, String refreshToken) {
        String key = "refreshToken:" + username + ":" + role + ":" + DigestUtils.sha256Hex(accessToken);
        redisUtil.setDataExpire(key, refreshToken, 1000L * 60 * 60 * 24 * 14);
        log.info("[AuthService] 리프레시 토큰을 Redis에 저장: {}", key);
    }


}
