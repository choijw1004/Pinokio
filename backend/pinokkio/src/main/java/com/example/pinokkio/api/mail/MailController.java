package com.example.pinokkio.api.mail;

import com.example.pinokkio.api.mail.request.MailAuthRequest;
import com.example.pinokkio.api.mail.request.MailRequest;
import com.example.pinokkio.api.mail.response.MailAuthResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    /**
     * 이메일로 인증 번호를 전송한다.
     * @param mailRequest   이메일을 포함한 요청 객체
     * @return              성공 20O OK , 실패 500 ERROR
     */
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

    /**
     * 인증 번호가 유효한지 확인한다.
     * @param mailAuthRequest   사용자 입력 인증 번호
     * @return                  인증 번호의 유효성 여부
     */
    @PostMapping("/api/mail/check-auth")
    public ResponseEntity<MailAuthResponse> checkAuth(@RequestBody MailAuthRequest mailAuthRequest) {
        boolean isAuthenticated = mailService.isAuthenticated(mailAuthRequest.getAuthNum());
        return ResponseEntity.ok(new MailAuthResponse(isAuthenticated));
    }

}
