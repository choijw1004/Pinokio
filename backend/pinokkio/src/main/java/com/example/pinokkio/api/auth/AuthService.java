package com.example.pinokkio.api.auth;

import com.example.pinokkio.api.auth.dto.request.LoginRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpKioskRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpPosRequest;
import com.example.pinokkio.api.auth.dto.request.SignUpTellerRequest;
import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.mail.MailService;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.pos.code.Code;
import com.example.pinokkio.api.pos.code.CodeRepository;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.config.RedisUtil;
import com.example.pinokkio.config.jwt.JwtTokenProvider;
import com.example.pinokkio.exception.badInput.PasswordBadInputException;
import com.example.pinokkio.exception.base.AuthenticationException;

import com.example.pinokkio.exception.confilct.EmailConflictException;
import com.example.pinokkio.exception.notFound.CodeNotFoundException;
import com.example.pinokkio.exception.notFound.PosNotFoundException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CodeRepository codeRepository;
    private final PosRepository posRepository;
    private final KioskRepository kioskRepository;
    private final TellerRepository tellerRepository;

    //랜덤 ID 생성기
    private static final String DIGITS = "0123456789";
    private static final Random RANDOM = new SecureRandom();


    /**
     * 가맹 코드, 이메일, 비밀번호, 비밀번호 확인 정보를 바탕으로 회원가입을 진행한다.
     * @param signUpPosRequest 포스 회원가입을 위한 Dto
     */
    @Transactional
    public void registerPos(SignUpPosRequest signUpPosRequest) {
        Code requestCode = checkValidateCode(signUpPosRequest.getCode());
        checkDuplicateEmail(signUpPosRequest.getUsername(), "ROLE_POS");
        checkConfirmPassword(signUpPosRequest.getPassword(), signUpPosRequest.getConfirmPassword());
        Pos pos = Pos.builder()
                .email(signUpPosRequest.getUsername())
                .password(passwordEncode(signUpPosRequest.getPassword()))
                .code(requestCode)
                .build();
        posRepository.save(pos);
    }

    /**
     * 가맹 코드, 이메일, 비밀번호, 비밀번호 확인 정보를 바탕으로 회원가입을 진행한다.
     * @param signUpTellerRequest 상담원 회원가입을 위한 Dto
     */
    public void registerTeller(SignUpTellerRequest signUpTellerRequest) {
        Code requestCode = checkValidateCode(signUpTellerRequest.getCode());
        checkDuplicateEmail(signUpTellerRequest.getUsername(), "ROLE_TELLER");
        checkConfirmPassword(signUpTellerRequest.getPassword(), signUpTellerRequest.getConfirmPassword());
        Teller teller = Teller.builder()
                .email(signUpTellerRequest.getUsername())
                .password(passwordEncode(signUpTellerRequest.getPassword()))
                .code(requestCode)
                .build();
        tellerRepository.save(teller);
    }

    /**
     * email = 코드 이름 + 숫자 4자리
     * password = 숫자 4자리
     * @param signUpKioskRequest 키오스크 회원가입을 위한 Dto
     */
    public void registerKiosk(SignUpKioskRequest signUpKioskRequest) {
        String posId = signUpKioskRequest.getPosId();
        Pos findPos = posRepository
                .findById(UUID.fromString(posId))
                .orElseThrow(() -> new PosNotFoundException(posId));

        String randomEmail = randomEmail(findPos);
        String randomPassword = randomPassword();

        Kiosk kiosk = Kiosk.builder()
                .pos(findPos)
                .email(randomEmail)
                .password(passwordEncode(randomPassword))
                .build();
        kioskRepository.save(kiosk);
    }

    @Transactional
    public AuthToken loginPos(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_POS", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "POS_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("POS 인증 실패", e.getMessage());
        }
    }

    @Transactional
    public AuthToken loginKiosk(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_KIOSK", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "KIOSK_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("KIOSK 인증 실패", e.getMessage());
        }
    }

    @Transactional
    public AuthToken loginTeller(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName(), "ROLE_TELLER", new Date());
            String refreshToken = jwtTokenProvider.createRefreshToken(new Date());
            // 리프레시 토큰을 Redis에 저장
            saveRefreshTokenToRedis(authentication.getName(), "TELLER_ROLE", accessToken, refreshToken);
            return new AuthToken(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("TELLER 인증 실패", e.getMessage());
        }
    }

    /**
     * 토큰 정보를 기반으로 리프레시 토큰을 Redis 에 저장한다.
     * @param username      유저 아이디(email)
     * @param role          유저 타입
     * @param accessToken   엑세스 토큰 정보
     * @param refreshToken  리프레시 토큰 정보
     */
    private void saveRefreshTokenToRedis(String username, String role, String accessToken, String refreshToken) {
        String key = "refreshToken:" + username + ":" + role + ":" + DigestUtils.sha256Hex(accessToken);
        redisUtil.setDataExpire(key, refreshToken, 1000L * 60 * 60 * 24 * 14);
        log.info("[AuthService] 리프레시 토큰을 Redis에 저장: {}", key);
    }

    /**
     * 아이디 중복인 경우 EmailConflictException 을 발생시킨다.
     * @param email 이메일
     * @param role  유저 타입
     */
    public void checkDuplicateEmail(String email, String role) {
        Map<String, Predicate<String>> roleCheckers = Map.of(
                "ROLE_POS", posRepository::existsByEmail,
                "ROLE_TELLER", tellerRepository::existsByEmail,
                "ROLE_KIOSK", kioskRepository::existsByEmail
        );

        Predicate<String> emailChecker = roleCheckers.get(role);
        if (emailChecker != null && emailChecker.test(email)) {
            throw new EmailConflictException(email);
        }
    }

    /**
     * 코드가 유효하지 않은 경우 CodeNotFoundException 을 발생시킨다.
     * @param code 코드
     */
    public Code checkValidateCode(String code) {
        UUID codeId = UUID.fromString(code);
        return codeRepository
                .findById(codeId)
                .orElseThrow(() -> new CodeNotFoundException(codeId.toString()));
    }

    /**
     * 입력받은 두 비밀번호가 같지 않으면 PasswordBadInputException 을 발생시킨다.
     * @param password          비밀번호
     * @param confirmPassword   비밀번호 확인
     */
    public void checkConfirmPassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) throw new PasswordBadInputException(password);
    }

    /**
     * BCryptPasswordEncoder 로 비밀번호를 암호화한다.
     * @param password 비밀번호
     * @return 암호화된 비밀번호
     */
    public String passwordEncode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 입력받은 포스의 키오스크 아이디를 랜덤으로 생성한다.
     * @param pos 키오스크의 출처 포스
     * @return 랜덤 생성된 키오스크 아이디
     */
    public String randomEmail(Pos pos) {
        StringBuilder sb = new StringBuilder(pos.getCode().getName());
        for (int i = 0; i < 4; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 숫자 4자리 비밀번호를 랜덤 생성한다.
     * @return 숫자 4자리 비밀번호
     */
    public String randomPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(index));
        }
        return sb.toString();
    }

}