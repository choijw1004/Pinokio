package com.example.pinokkio.api.pos;

import com.example.pinokkio.api.pos.dto.response.PosResponse;
import com.example.pinokkio.exception.domain.pos.PosEmailNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PosService {

    private final PosRepository posRepository;

    public PosResponse getMyPosInfo(String email) {
        Pos pos = posRepository
                .findByEmail(email)
                .orElseThrow(() -> new PosEmailNotFoundException(email));
        return new PosResponse(pos.getCode().getName(), pos.getId().toString(), pos.getEmail());
    }

    public Pos getPosByEmail(String email) {
        return posRepository
                .findByEmail(email)
                .orElseThrow(() -> new PosEmailNotFoundException(email));
    }

    public boolean isEmailDuplicated(String email) {
        return posRepository.existsByEmail(email);
    }
}
