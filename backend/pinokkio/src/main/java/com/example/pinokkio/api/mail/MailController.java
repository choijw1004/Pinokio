package com.example.pinokkio.api.mail;

import com.example.pinokkio.api.mail.request.MailAuthRequest;
import com.example.pinokkio.api.mail.request.MailRequest;
import com.example.pinokkio.api.mail.response.MailAuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Mail Controller", description = "메일 인증 관련 API")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "인증 메일 전송", description = "이메일로 인증 번호를 전송.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메일 전송 성공"),
            @ApiResponse(responseCode = "500", description = "메일 전송 실패",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/api/mail/send")
    public ResponseEntity<?> sendMail(@RequestBody MailRequest mailRequest) {
        try {
            String authNum = mailService.sendEmail(mailRequest.getEmail());
            log.info("Auth Number: {}", authNum);
            return ResponseEntity.ok().build();
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }

    @Operation(summary = "인증 번호 확인", description = "인증 번호의 유효성을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 번호 확인 완료",
                    content = @Content(schema = @Schema(implementation = MailAuthResponse.class)))
    })
    @GetMapping("/api/mail/check-auth")
    public ResponseEntity<MailAuthResponse> checkAuth(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "인증 번호 확인 요청", required = true,
                    content = @Content(schema = @Schema(implementation = MailAuthRequest.class)))
            @RequestBody MailAuthRequest mailAuthRequest) {
        boolean isAuthenticated = mailService.isAuthenticated(mailAuthRequest.getAuthNum());
        return ResponseEntity.ok(new MailAuthResponse(isAuthenticated));
    }
}