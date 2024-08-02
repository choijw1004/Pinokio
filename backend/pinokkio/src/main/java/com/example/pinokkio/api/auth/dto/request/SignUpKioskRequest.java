package com.example.pinokkio.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Schema(description = "키오스크 회원가입 요청 DTO")
public class SignUpKioskRequest {
    @Schema(description = "POS ID", example = "UUID 타입", required = true)
    private String posId;
}