package com.example.pinokkio.api.pos;

import com.example.pinokkio.api.pos.code.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PosRepository extends JpaRepository<Pos, UUID> {

    /**
     * 해당 이메일로 가입된 포스를 반환한다.
     * @param email 이메일 정보
     * @return 가입된 포스 정보
     */
    Optional<Pos> findByEmail(String email);

    /**
     * 해당 이메일로 가입된 포스 존재 여부를 확인한다.
     * @param email 이메일 정보
     * @return 가입 여부 정보
     */
    boolean existsByEmail(String email);

}
