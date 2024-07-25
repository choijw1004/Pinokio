package com.example.pinokkio.api.room;

import com.example.pinokkio.api.teller.Teller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    Optional<Room> findRoomByRoomId(UUID roomId);

    Optional<Room> findByTeller(Teller teller);

    List<Room> findAllByOrderByNumberOfCustomersAsc();

    //    void deleteAllByRoomId(UUID roomId);
    void deleteByTeller(Teller teller);
}

