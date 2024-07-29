package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.LoginRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpKioskRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpPosRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpTellerRequest;
import com.example.pinokkio.exception.base.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/register/pos")
    public ResponseEntity<?> registerPos(@Validated @RequestBody SignUpPosRequest signUpPosRequest) {
        authService.registerPos(signUpPosRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register/teller")
    public ResponseEntity<?> registerTeller(@Validated @RequestBody SignUpTellerRequest signUpTellerRequest) {
        authService.registerTeller(signUpTellerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping("/register/kiosk")
    public ResponseEntity<?> registerKiosk(@Validated @RequestBody SignUpKioskRequest signUpKioskRequest) {
        authService.registerKiosk(signUpKioskRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login/pos")
    public ResponseEntity<?> loginPos(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginPos(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/login/teller")
    public ResponseEntity<?> loginTeller(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginTeller(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/login/kiosk")
    public ResponseEntity<?> loginKiosk(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginKiosk(loginRequest);
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