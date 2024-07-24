package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.LoginRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpKioskRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpPosRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpTellerRequest;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.pos.code.Code;
import com.example.pinokkio.api.pos.code.CodeRepository;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.config.RedisUtil;
import com.example.pinokkio.config.jwt.JwtTokenProvider;
import com.example.pinokkio.exception.base.AuthenticationException;

import com.example.pinokkio.exception.notFound.CodeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CodeRepository codeRepository;
    private final PosRepository posRepository;
    private final KioskRepository kioskRepository;
    private final TellerRepository tellerRepository;


    // 회원가입 로직 구현
    @Transactional
    public void registerPos(SignUpPosRequest signUpPosRequest) {
        //코드 UUID 검증
        UUID codeId = UUID.fromString(signUpPosRequest.getCode());
        Code code = codeRepository.findById(codeId).orElseThrow(() -> new CodeNotFoundException(codeId));

        //이메일 중복 검증
        String email
    }

    public void registerTeller(SignUpTellerRequest signUpTellerRequest) {
        // 회원가입 로직 구현
    }

    public void registerKiosk(SignUpKioskRequest signUpKioskRequest) {
        // 회원가입 로직 구현
    }

    @Transactional
    public AuthToken loginPos(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_POS", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "POS_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("POS 인증 실패", e.getMessage());
        }
    }

    /**
     * Kiosk 로그인 -> AuthToken 반환
     */
    @Transactional
    public AuthToken loginKiosk(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_KIOSK", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "KIOSK_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("KIOSK 인증 실패", e.getMessage());
        }
    }

    /**
     * Teller 로그인 -> AuthToken 반환
     */
    @Transactional
    public AuthToken loginTeller(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_TELLER", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "TELLER_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("TELLER 인증 실패", e.getMessage());
        }
    }

    /**
     * refreshToken -> Redis
     */
    private void saveRefreshTokenToRedis(String username, String role, String accessToken, String refreshToken) {
        String key = "refreshToken:" + username + ":" + role + ":" + DigestUtils.sha256Hex(accessToken);
        redisUtil.setDataExpire(key, refreshToken, 1000L * 60 * 60 * 24 * 14);
        log.info("[AuthService] 리프레시 토큰을 Redis에 저장: {}", key);
    }

    public boolean duplicateEmail(String email, String role) {
        if(role.equals("ROLE_POS")) {

        }
        else if(role.equals("ROLE_TELLER")) {

        }
        else {

        }
        return true;
    }

}