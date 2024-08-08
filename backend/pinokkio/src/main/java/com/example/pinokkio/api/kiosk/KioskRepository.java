package com.example.pinokkio.api.kiosk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KioskRepository extends JpaRepository<Kiosk, UUID> {
    /**
     * 해당 이메일로 가입된 키오스크를 반환한다.
     * @param email 이메일 정보
     * @return 가입 여부 정보
     */
    Optional<Kiosk> findByEmail(String email);

    /**
     * 해당 이메일로 가입된 키오스크 존재 여부를 확인한다.
     * @param email 이메일 정보
     * @return 가입 여부 정보
     */
    boolean existsByEmail(String email);

    List<Kiosk> findAllByPosId(UUID posId);

    Optional<Kiosk> findByIdAndPosId(UUID id, UUID posId);
}
