package com.example.pinokkio.api.kiosk.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KioskResponse {
    private String kioskId;
    private String posId;
    private String email;

    public KioskResponse(String kioskId, String posId, String email) {
        this.kioskId = kioskId;
        this.posId = posId;
        this.email = email;
    }

}
