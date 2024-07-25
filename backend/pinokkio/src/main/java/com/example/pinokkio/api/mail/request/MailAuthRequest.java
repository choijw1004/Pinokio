package com.example.pinokkio.api.mail.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MailAuthRequest {
    private String authNum;
}
