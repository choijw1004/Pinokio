package com.example.pinokkio.config.jwt;

public enum Role {
    P("ROLE_POS"),
    K("KIOSK"),
    T("ROLE_TELLER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}