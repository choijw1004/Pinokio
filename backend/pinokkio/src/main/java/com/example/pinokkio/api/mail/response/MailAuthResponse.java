package com.example.pinokkio.api.mail.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailAuthResponse {
    private boolean isAuthenticated;
}
