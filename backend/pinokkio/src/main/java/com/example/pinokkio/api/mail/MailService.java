package com.example.pinokkio.api.mail;

import com.example.pinokkio.config.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String configEmail;

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;


    //난수 생성후 Redis 에 5분간 저장
    private String createCode() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();

        String authNum = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        redisUtil.setDataExpire(authNum, authNum, Duration.ofMinutes(5).toMinutes());
        return authNum;
    }


    //메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        String authNum = createCode();
        log.info("authNum ={}", authNum);

        String title = "Pinokkio 인증 번호 안내";
        MimeMessage message = javaMailSender.createMimeMessage();
        log.info("message 생성 = {}", message);

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        log.info("message 설정 완료");

        String msgg = "";
        msgg += "<p style=\"font-size:10pt;font-family:sans-serif;padding:0 0 0 10pt\"><br></p>";
        msgg += "<p>";
        msgg += "</p><table align=\"center\" width=\"670\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top: 2px solid #60b9ce; border-right: 1px solid #e7e7e7; border-left: 1px solid #e7e7e7; border-bottom: 1px solid #e7e7e7;\">";
        msgg += "<tbody><tr><td style=\"background-color: #ffffff; padding: 40px 30px 0 35px; text-align: center;\">";
        msgg += "<table width=\"605\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"text-align: left; font-family: '맑은 고딕','돋움';\">";
        msgg += "<tbody><tr><td style=\"color: #2daad1; font-size: 25px; text-align: left; width: 352px; word-spacing: -1px; vertical-align: top;\">";
        msgg += "인증 번호 확인 후<br>";
        msgg += "이메일 인증을 완료해 주세요.";
        msgg += "</td></tr>";
        msgg += "<tr><td style=\"font-size: 17px; vertical-align: bottom; height: 27px;\">안녕하세요? \"Pinokio\"입니다.</td></tr>";
        msgg += "<tr><td colspan=\"2\" style=\"font-size: 13px; word    -spacing: -1px; height: 30px;\">아래 인증번호를 입력하시고 계속 진행해 주세요.</td></tr></tbody></table>";
        msgg += "</td></tr>";
        msgg += "<tr><td style=\"padding: 39px 196px 70px;\">";
        msgg += "<table width=\"278\" style=\"background-color: #3cbfaf; font-family: '맑은 고딕','돋움';\">";
        msgg += "<tbody><tr><td height=\"49\" style=\"text-align: center; color: #fff\">인증번호 : <span>" + authNum + "</span></td></tr>";
        msgg += "</tbody></table>";
        msgg += "</td></tr>";
        msgg += "</tbody></table>";
        msgg += "<p></p>";
        msgg += "<img height=\"1\" width=\"1\" border=\"0\" style=\"display:none;\" src=\"http://ems.midasit.com:4121/7I-110098I-41E-8174224742I-4uPmuPzeI-4I-3\">";

        //보내는 이메일 및 메타데이터
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(configEmail,"Pinokkio"));
        log.info("metadata 생성");
        return message;
    }

    //메일 전송
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        log.info("sendEamil 호출");
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        log.info("createEmailForm 호출");
        javaMailSender.send(emailForm);
        log.info("send호출");
        //Redis 에서 인증번호 가져오기
        return redisUtil.getData(emailForm.getSubject());
    }

}