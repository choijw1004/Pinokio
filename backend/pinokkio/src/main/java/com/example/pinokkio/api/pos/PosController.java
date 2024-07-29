package com.example.pinokkio.api.pos;

import com.example.pinokkio.api.pos.dto.response.PosResponse;
import com.example.pinokkio.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final JwtProvider jwtProvider;

    /**
     * 요청한 포스의 자기 정보 조회
     * [가맹점 이름, 포스 ID, 포스 이메일]
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/my-info")
    public ResponseEntity<?> getPos() {
        PosResponse posResponse = posService.getMyPosInfo(jwtProvider.getCurrentUserEmail());
        return ResponseEntity.ok(posResponse);
    }






}
