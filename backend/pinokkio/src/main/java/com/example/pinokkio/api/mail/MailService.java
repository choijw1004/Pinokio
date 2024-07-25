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


    /**
     * 이메일 인증 용도로 사용되는 랜덤 인증번호를 생성하여 반환한다.
     *
     * @return 랜덤으로 생성된 인증번호
     */
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


    /**
     * 수신 이메일 대상으로 하는 메시지를 생성한다.
     *
     * @param email 수신 이메일
     * @return 생성된 메시지
     * @throws MessagingException           이메일 메시지 생성 중 오류가 발생한 경우
     * @throws UnsupportedEncodingException 이메일 메시지의 인코딩이 지원되지 않는 경우
     */
    public MimeMessage createEmailForm(String email, String authNum) throws MessagingException, UnsupportedEncodingException {
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
        message.setFrom(new InternetAddress(configEmail, "Pinokkio"));
        log.info("metadata 생성");
        return message;
    }

    /**
     * 수신 이메일 대상으로 인증 번호를 전송한다.
     *
     * @param toEmail 수신 이메일
     * @return 랜덤으로 생성된 인증 번호
     * @throws MessagingException           이메일 메시지 생성 중 오류가 발생한 경우
     * @throws UnsupportedEncodingException 이메일 메시지의 인코딩이 지원되지 않는 경우
     */
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        log.info("sendEmail()");
        String authNum = createCode();
        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        log.info("createEmailForm()");
        log.info(String.valueOf(emailForm));
        javaMailSender.send(emailForm);
        log.info("send()");
        log.info("[sendEmail] return {}", redisUtil.getData(authNum));
        return redisUtil.getData(authNum);
    }

    /**
     * 주어진 인증 번호가 유효한지 확인한다.
     *
     * @param authNum 인증 번호
     * @return 인증 확인 유무
     */
    public boolean isAuthenticated(String authNum) {
        String findData = redisUtil.getData(authNum);
        log.info("[isAuthenticated] authNum: {}, findData: {}", authNum, findData);
        return findData != null;
    }

}