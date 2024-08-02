package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.LoginRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpKioskRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpPosRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpTellerRequest;
import com.example.pinokkio.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller", description = "로그인 및 회원가입 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "POS 회원가입", description = "POS 사용자를 위한 회원가입을 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/register/pos")
    public ResponseEntity<?> registerPos(@Validated @RequestBody SignUpPosRequest signUpPosRequest) {
        authService.registerPos(signUpPosRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "상담원 회원가입", description = "상담원을 위한 회원가입을 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/register/teller")
    public ResponseEntity<?> registerTeller(@Validated @RequestBody SignUpTellerRequest signUpTellerRequest) {
        authService.registerTeller(signUpTellerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "키오스크 등록", description = "POS 사용자가 키오스크를 등록,  ROLE_POS인 사용자가 Header에 Access Token을 가진 경우만 접근가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "키오스크 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping("/register/kiosk")
    public ResponseEntity<?> registerKiosk(@Validated @RequestBody SignUpKioskRequest signUpKioskRequest) {
        authService.registerKiosk(signUpKioskRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "POS 로그인", description = "POS 사용자 로그인을 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthToken.class))
            ),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login/pos")
    public ResponseEntity<?> loginPos(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginPos(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "상담원 로그인", description = "상담원 로그인을 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthToken.class))
            ),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login/teller")
    public ResponseEntity<?> loginTeller(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginTeller(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "키오스크 로그인", description = "키오스크 로그인을 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthToken.class))
            ),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login/kiosk")
    public ResponseEntity<?> loginKiosk(@Validated @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.loginKiosk(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발급", description = "refresh 토큰을 이용한 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthToken.class))
            ),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/refresh")
    public ResponseEntity<?> tokenReissue(HttpServletRequest request) {
        String refresh = request.getHeader("refresh");
        AuthToken authToken = authService.reissue(refresh);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken());
        return new ResponseEntity<>(authToken, httpHeaders, HttpStatus.OK);
    }
}