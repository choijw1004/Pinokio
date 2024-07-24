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

    /**
     * 더이상 사용되지 않는다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        throw new UsernameNotFoundException("사용자 역할을 지정하여 사용해야 합니다.");
    }

    /**
     * 커스텀 loadUserByUsernameAndRole
     */
    public CustomUserDetail loadUserByUsernameAndRole(String email, String role) throws UsernameNotFoundException {
        log.info("loadUserByUsernameAndRole");
        switch (role) {
            case "ROLE_POS":
                return posRepository.findByEmail(email)
                        .map(pos -> new CustomUserDetail(pos.getEmail(), pos.getPassword(), role))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : POS 사용자 존재하지 않음"));

            case "ROLE_KIOSK":
                return kioskRepository.findByEmail(email)
                        .map(kiosk -> new CustomUserDetail(kiosk.getEmail(), kiosk.getPassword(), role))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : KIOSK 사용자 존재하지 않음"));

            case "ROLE_TELLER":
                return tellerRepository.findByEmail(email)
                        .map(teller -> new CustomUserDetail(teller.getEmail(), teller.getPassword(), role))
                        .orElseThrow(() -> new UsernameNotFoundException(email + " : TELLER 사용자 존재하지 않음"));

            default:
                throw new UsernameNotFoundException(role + " : 알 수 없는 역할");
        }
    }
}
