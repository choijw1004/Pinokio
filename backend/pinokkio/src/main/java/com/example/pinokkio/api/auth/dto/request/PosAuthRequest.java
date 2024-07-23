package com.example.pinokkio.api.auth.dto.request;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class PosAuthRequest {
    private String code;
    private String username;
    private String password;
}
