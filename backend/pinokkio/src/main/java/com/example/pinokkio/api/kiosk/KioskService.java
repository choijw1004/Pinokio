package com.example.pinokkio.api.kiosk;

import com.example.pinokkio.api.kiosk.grpc.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KioskService {

    private KioskRepository kioskRepository;


    /**
     * gRPC 로그인 요청을 처리합니다.
     *
     * @param kioskId 키오스크 ID
     * @return LoginResponse 객체
     */
    public LoginResponse handleGrpcLogin(UUID kioskId) {
        log.info("Handling gRPC login for kiosk ID: {}", kioskId);
        try {
            log.info("Looking up kiosk with ID: {}", kioskId);
            Kiosk kiosk = kioskRepository.findById(kioskId).orElse(null);
            if (kiosk != null) {
                log.info("Kiosk found: {}", kiosk);
                return LoginResponse.newBuilder().setMessage("Login successful").build();
            } else {
                log.warn("Kiosk not found for ID: {}", kioskId);
                return LoginResponse.newBuilder().setMessage("Login failed").build();
            }
        } catch (NumberFormatException e) {
            log.error("Invalid kiosk ID format: {}", kioskId, e);
            return LoginResponse.newBuilder().setMessage("Invalid kiosk ID").build();
        }
    }

}