package com.example.pinokkio.api.room;

import com.example.pinokkio.api.teller.Teller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    void deleteByTeller(Teller teller);

    Optional<Room> findByTeller(Teller teller);
}

