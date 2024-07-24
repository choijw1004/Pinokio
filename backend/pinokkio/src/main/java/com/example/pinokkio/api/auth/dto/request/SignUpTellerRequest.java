package com.example.pinokkio.api.auth.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class SignUpTellerRequest {
    private String code;
    private String username;
    private String password;
    private String confirmPassword;
}
