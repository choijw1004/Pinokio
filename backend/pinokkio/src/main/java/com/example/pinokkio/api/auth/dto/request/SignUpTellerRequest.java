package com.example.pinokkio.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Schema(description = "상담원 회원가입 요청 DTO")
public class SignUpTellerRequest {
    @Schema(description = "매장 코드", example = "UUID 타입", required = true)
    private String code;

    @Schema(description = "사용자 이메일", example = "user@ssafy.com", required = true)
    private String username;

    @Schema(description = "비밀번호", example = "password123", required = true)
    private String password;

    @Schema(description = "비밀번호 확인", example = "password123", required = true)
    private String confirmPassword;
}