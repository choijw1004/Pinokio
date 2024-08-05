package com.example.pinokkio.api.teller;

import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.pos.dto.response.PosResponse;
import com.example.pinokkio.exception.domain.pos.PosEmailNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TellerService {

    private final TellerRepository tellerRepository;

    public boolean isEmailDuplicated(String email) {
        return tellerRepository.existsByEmail(email);
    }
}
