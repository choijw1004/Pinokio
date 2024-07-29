package com.example.pinokkio.api.room;

import com.example.pinokkio.api.teller.Teller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findAllByOrderByNumberOfCustomersAsc();

    void deleteByTeller(Teller teller);
}

