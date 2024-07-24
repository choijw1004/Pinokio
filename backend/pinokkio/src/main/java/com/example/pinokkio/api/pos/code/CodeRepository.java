package com.example.pinokkio.api.pos.code;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
    boolean existsById(@NotNull UUID id);
}
