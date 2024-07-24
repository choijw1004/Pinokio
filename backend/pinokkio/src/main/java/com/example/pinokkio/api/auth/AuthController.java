package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.KioskAuthRequest;
import com.example.pinokkio.api.auth.dto.request.PosAuthRequest;
import com.example.pinokkio.api.auth.dto.request.TellerAuthRequest;
import com.example.pinokkio.exception.base.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/pos")
    public ResponseEntity<?> authorizePos(@Validated @RequestBody PosAuthRequest posAuthRequest) {
        AuthToken authToken = authService.authenticatePos(posAuthRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/api/auth/kiosk")
    public ResponseEntity<?> authorizeKiosk(@Validated @RequestBody KioskAuthRequest kioskAuthRequest) {
        AuthToken authToken = authService.authenticateKiosk(kioskAuthRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/api/auth/teller")
    public ResponseEntity<?> authorizeTeller(@Validated @RequestBody TellerAuthRequest tellerAuthRequest) {
        AuthToken authToken = authService.authenticateTeller(tellerAuthRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        log.error("인증 중 오류 발생: ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}