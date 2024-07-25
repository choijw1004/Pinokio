package com.example.pinokkio.config.jwt;

import com.example.pinokkio.exception.auth.ExpiredTokenException;
import com.example.pinokkio.exception.auth.TokenNotValidException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final CustomUserDetailService customUserDetailService;
    private final long accessValidTime = 1000L * 60 * 60;    // 액세스 토큰 유효 시간 60분
    private final long refreshValidTime = 1000L * 60 * 60 * 24 * 14;    // 리프레쉬 토큰 유효 시간 2주

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * secretKey를 Base64로 인코딩
     */
    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    /**
     * AccessToken 생성
     * role 포함
     */
    public String createAccessToken(String email, String role, Date now) {
        log.info("[createAccessToken] 액세스 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        log.info("Access Token에서 claims 생성 claims : {}", claims);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuer("pinokkio")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("[createToken] 액세스 토큰 생성 완료: {}", accessToken);
        return accessToken;
    }

    /**
     * RefreshToken 생성
     * role 포함하지 않고 최소한의 정보만 가진다
     */
    public String createRefreshToken(Date now) {
        log.info("[createRefreshToken] 리프레쉬 토큰 생성 시작");
        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("pinokkio")
                .setExpiration(new Date(now.getTime() + refreshValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("[createRefreshToken] 리프레쉬 토큰 생성 완료: {}", refreshToken);
        return refreshToken;
    }

    /**
     * AccessToken 추출
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            log.info("[resolveToken] HTTP 헤더에서 Token 값 추출 완료: {}", token);
            return token;
        }
        return null;
    }

    /**
     * AccessToken 로부터 Authentication 객체를 얻어온다.
     */
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        String newInput = parseAndFormatRoleEmail(token);
        UserDetails customUserDetail = customUserDetailService.loadUserByUsername(newInput);

        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails User Email : {}", customUserDetail.getUsername());
        return new UsernamePasswordAuthenticationToken(customUserDetail, token, customUserDetail.getAuthorities());
    }

    /**
     * AccessToken 에서 유저정보 추출
     */
    public UserDetails getUserFromAccessToken(String accessToken) {
        try {
            String newInput = parseAndFormatRoleEmail(accessToken);
            return customUserDetailService.loadUserByUsername(newInput);
        } catch (ExpiredJwtException e) {
            String email = e.getClaims().getSubject();
            String role = (String) e.getClaims().get("role");
            Role roleEnum = Role.valueOf(role);
            String newInput = roleEnum + email;
            return customUserDetailService.loadUserByUsername(newInput);
        }
    }

    /**
     * AccessToken의 유효성 + 만료여부 체크
     */
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new TokenNotValidException();
        }
        return true;
    }

    /**
     * AccessToken 에서 Email 추출
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * AccessToken 에서 Role 추출
     */
    public String getRoleFromToken(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role");
    }

    /**
     * 토큰으로부터 이메일(아이디)값과 role 값을 파싱해 "R + 이메일" 형태로 제공
     * R 
     * - "P" : ROLE_POS
     * - "K" : ROLE_KIOSK
     * - "T" : ROLE_TELLER
     */
    private String parseAndFormatRoleEmail(String token) {
        String email = getEmailFromToken(token);
        String role = getRoleFromToken(token);
        log.info("[parseToken] email: {}, role: {}", email, role);
        Role parseRole = Role.fromValue(role);

        String parseToken = parseRole + email;
        log.info("[reformat parseToken] parseToken: {}", parseToken);
        return parseToken;
    }
}
