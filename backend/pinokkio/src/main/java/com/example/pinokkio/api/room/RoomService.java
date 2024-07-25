package com.example.pinokkio.api.room;

import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.exception.notFound.KioskNotFoundException;
import com.example.pinokkio.exception.notFound.RoomNotFoundException;
import com.example.pinokkio.exception.notFound.TellerNotFoundException;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final int MAX_CAPACITY = 3;

    private final RoomRepository roomRepository;
    private final TellerRepository tellerRepository;
    private final KioskRepository kioskRepository;

    @Transactional
    public AccessToken createRoom(AccessToken roomToken, String tellerId) {

        Teller teller = getEntityById(tellerRepository, tellerId, TellerNotFoundException::new);
        // Room 생성
        Room room = Room.builder()
                .teller(teller)
                .build();
        roomRepository.save(room);

        // 토큰 발급
        String roomId = room.getRoomId().toString();
        roomToken.setName(roomId);
        roomToken.setIdentity(tellerId);
        roomToken.addGrants(new RoomJoin(true), new RoomName(roomId));

        log.info("[createRoom] roomId: {}, tellerId: {}", roomId, tellerId);
        return roomToken;
    }

    @Transactional
    public void deleteRoom(String tellerId) {

        Teller teller = getEntityById(tellerRepository, tellerId, TellerNotFoundException::new);
        roomRepository.deleteByTeller(teller);

        log.info("[deleteRoom] complete");
    }

    @Transactional
    public AccessToken enterRoom(AccessToken roomToken, String kioskId) {

        Kiosk kiosk = getEntityById(kioskRepository, kioskId, KioskNotFoundException::new);

        // 입장 가능한 방 찾기
        String roomId = getAvailableRoom();
        log.info("[enterRoom] available roomId: {}", roomId);

        //TODO 토큰 생성 시 바로 입장하는지 확인, 아니라면 noc 증가시키는 로직은 분리해야 함
        Room room = getEntityById(roomRepository, roomId, RoomNotFoundException::new);
        room.setNumberOfCustomers(room.getNumberOfCustomers() + 1);
        roomRepository.save(room);

        // 토큰 발급
        roomToken.setName(roomId);
        roomToken.setIdentity(kioskId);
        roomToken.addGrants(new RoomJoin(true), new RoomName(roomId));

        log.info("[enterRoom] roomId: {}, kioskId: {}", roomId, kioskId);
        return roomToken;
    }

    private String getAvailableRoom() {

        List<Room> availableRooms = roomRepository.findAllByOrderByNumberOfCustomersAsc();

        Room firstRoom = availableRooms.getFirst();
        if (firstRoom.getNumberOfCustomers() < MAX_CAPACITY) {
            return firstRoom.getRoomId().toString();
        }

        log.info("[getAvailableRoom] 모든 방이 {}명 상담 중", MAX_CAPACITY);
        //TODO 상담 불가한 경우의 예외 생성
        throw new RuntimeException("상담 가능한 방이 없습니다.");
    }

    @Transactional
    public void leaveRoom(String roomId) {
        Room room = getEntityById(roomRepository, roomId, RoomNotFoundException::new);
        int curCusNum = room.getNumberOfCustomers();
        log.info("[leaveRoom] Number of Customers: {}", curCusNum);

        if (curCusNum > 0) {
            room.setNumberOfCustomers(curCusNum - 1);
            log.info("[leaveRoom] Number of Customers: {}", curCusNum - 1);
            roomRepository.save(room);
        }
    }


    /**
     * 제네릭 메서드를 사용하여 다양한 엔티티를 조회하는 로직
     */
    private <T, ID> T getEntityById(JpaRepository<T, UUID> repository, String id, Function<UUID, RuntimeException> exceptionSupplier) {
        UUID uuid = UUID.fromString(id);
        return repository.findById(uuid)
                .orElseThrow(() -> exceptionSupplier.apply(uuid));
    }
}
