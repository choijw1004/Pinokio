package com.example.pinokkio.api.kiosk;

import com.example.pinokkio.api.kiosk.dto.response.KioskResponse;
import com.example.pinokkio.api.kiosk.grpc.LoginResponse;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.dto.response.PosResponse;
import com.example.pinokkio.exception.domain.pos.PosEmailNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KioskService {

    private final KioskRepository kioskRepository;

    /**
     * 입력받은 이메일에 해당하는 키오스크의 핵심 필드를 반환한다.
     *
     * @param email 이메일 정보
     * @return KioskResponse
     */
    public KioskResponse getMyKioskInfo(String email) {
        Kiosk kiosk = kioskRepository
                .findByEmail(email)
                .orElseThrow(() -> new PosEmailNotFoundException(email));
        return new KioskResponse(
                kiosk.getPos().getId().toString(),
                kiosk.getId().toString(),
                kiosk.getEmail()
        );
    }

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