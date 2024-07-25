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

    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/my-info")
    public ResponseEntity<?> getPos() {
        PosResponse posResponse = posService.getMyPosInfo(jwtProvider.getCurrentUserEmail());
        return ResponseEntity.ok(posResponse);
    }

}
