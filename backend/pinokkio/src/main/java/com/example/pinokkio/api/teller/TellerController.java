package com.example.pinokkio.api.teller;

import com.example.pinokkio.api.auth.dto.response.EmailDuplicationResponse;
import com.example.pinokkio.api.pos.dto.response.PosResponse;
import com.example.pinokkio.config.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teller")
@Tag(name = "Teller Controller", description = "상담원 정보 관련 API")
public class TellerController {

    private final TellerService tellerService;

    /**
     * 요청 이메일의 중복 여부 검증
     */
    @Operation(summary = "중복 이메일 조회", description = "포스 내 중복 이메일 조회")
    @GetMapping("/duplicate/{email}")
    public ResponseEntity<?> checkEmailDuplication(@PathVariable String email) {
        EmailDuplicationResponse posResponse = new EmailDuplicationResponse(tellerService.isEmailDuplicated(email));
        return ResponseEntity.ok(posResponse);
    }
}
