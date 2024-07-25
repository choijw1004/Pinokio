package com.example.pinokkio.api.mail;

import com.example.pinokkio.api.mail.request.MailRequest;
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

    @PostMapping("/api/mail/send")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest) {
        try {
            String authNum = mailService.sendEmail(mailRequest.getEmail());
            log.info("authNum = {}", authNum);
            return new ResponseEntity<>(authNum, HttpStatus.OK);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
