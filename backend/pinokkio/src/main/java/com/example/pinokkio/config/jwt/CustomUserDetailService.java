package com.example.pinokkio.config.jwt;

import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.teller.TellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final PosRepository posRepository;
    private final KioskRepository kioskRepository;
    private final TellerRepository tellerRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        char role = input.charAt(0);
        String email = input.substring(1);
        log.info("role: " + role + " email: " + email);
        switch (role) {
            case 'P':
                return posRepository.findByEmail(email)
                        .map(pos -> new CustomUserDetail(pos.getEmail(), pos.getPassword(), "ROLE_POS"))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : POS 사용자 존재하지 않음"));

            case 'K':
                return kioskRepository.findByEmail(email)
                        .map(kiosk -> new CustomUserDetail(kiosk.getEmail(), kiosk.getPassword(), "ROLE_KIOSK"))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : KIOSK 사용자 존재하지 않음"));

            case 'T':
                return tellerRepository.findByEmail(email)
                        .map(teller -> new CustomUserDetail(teller.getEmail(), teller.getPassword(), "ROLE_TELLER"))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : TELLER 사용자 존재하지 않음"));

            default:
                throw new UsernameNotFoundException(role + " : 알 수 없는 역할");
        }
    }
}
