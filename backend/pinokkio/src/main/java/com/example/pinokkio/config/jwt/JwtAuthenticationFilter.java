package com.example.pinokkio.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        log.info("[JwtAuthenticationFilter] AccessToken 값 추출 완료: {}", accessToken);

        try {
            log.info("try 문 진입");
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                log.info("if 문 진입");
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.info("예외발생");
            request.setAttribute("exception", e);
        }

        log.info("doFilter");
        filterChain.doFilter(request, response);
    }
}
