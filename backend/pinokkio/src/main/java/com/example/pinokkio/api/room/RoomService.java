package com.example.pinokkio.api.room;

import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.exception.RoomNotAvailableException;
import com.example.pinokkio.exception.notFound.KioskNotFoundException;
import com.example.pinokkio.exception.notFound.RoomNotFoundException;
import com.example.pinokkio.exception.notFound.TellerNotFoundException;
import io.livekit.server.*;
import jakarta.annotation.PostConstruct;
import livekit.LivekitModels;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomService {
//TODO: 상담원, 고객 서비스 로직 분리
//TODO: 존재 여부 검증 당위성

    // 상담원의 최대 응대 고객 수
    private final int MAX_CAPACITY = 3;

    private final RoomRepository roomRepository;
    private final TellerRepository tellerRepository;
    private final KioskRepository kioskRepository;

    private RoomServiceClient roomServiceClient;

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    @PostConstruct
    public void init() {
        this.roomServiceClient = RoomServiceClient.create("http://localhost:7880/", LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
    }

    /**
     * 상담원 - 방 생성 후 입장을 위한 토큰 발급
     */
    @Transactional
    public AccessToken createRoom(String tellerId) {
        // 상담원 존재 여부 검증
        Teller teller = getEntityById(tellerRepository, tellerId, TellerNotFoundException::new);

        // 방 생성 후 DB 저장
        Room room = Room.builder().
                teller(teller).
                build();
        roomRepository.save(room);

        // 토큰 발급
        String roomId = room.getRoomId().toString();
        AccessToken roomToken = createToken(roomId, tellerId);
        log.info("[createRoom] roomId: {}, tellerId: {}", roomId, tellerId);

        return roomToken;
    }

    /**
     * 상담원 - 상담원 퇴장 시 방 삭제
     */
    @Transactional
    public void deleteRoom(String tellerId) {
        Teller teller = getEntityById(tellerRepository, tellerId, TellerNotFoundException::new);
        roomRepository.deleteByTeller(teller);
        log.info("[deleteRoom] complete for tellerId: {}", tellerId);
    }

    /**
     * 상담원 - 상담 요청 수락시 고객의 방 접근을 위한 roomId 정보 제공
     */
    public String acceptInvitation(String roomId, String tellerId, String kioskId) {
        Room room = getEntityById(roomRepository, roomId, RoomNotFoundException::new);
        Teller teller = getEntityById(tellerRepository, tellerId, TellerNotFoundException::new);
        Kiosk kiosk = getEntityById(kioskRepository, kioskId, KioskNotFoundException::new);

        log.info("[acceptInvitation] roomId: {}, participantName: {}", roomId, tellerId);
        return roomId;
    }

    /**
     * 상담원 - 상담 요청 거절
     */
    public void rejectInvitation(String roomId, String tellerId) {
        log.info("[rejectInvitation] roomId: {}, tellerId: {}", roomId, tellerId);
    }

    /**
     * 고객 -모든 방에 상담 요청 전송
     */
    public void messageAllRooms(String kioskId) {
        try {
            List<LivekitModels.Room> rooms = roomServiceClient.listRooms().execute().body();
            for (LivekitModels.Room room : rooms) {
                // 입장 요청 전송
                sendMessageToRoom(room, kioskId);
            }
        } catch (IOException e) {
            log.error("[messageAllRooms] Error: {}", e.getMessage());
            throw new RuntimeException("Failed to send message to all rooms", e);
        }
    }

    /**
     * 고객 - 입장
     */
    @Transactional
    public AccessToken enterRoom(String roomId, String kioskId) {
        // 고객 존재 여부 검증
        Kiosk kiosk = getEntityById(kioskRepository, kioskId, KioskNotFoundException::new);

        // 방 유효성 검증
        Room room = getEntityById(roomRepository, roomId, RoomNotFoundException::new);
        int currentCustomerCount = room.getNumberOfCustomers();
        if (currentCustomerCount >= MAX_CAPACITY) {
            throw new RoomNotAvailableException(UUID.fromString(roomId));
        }
        room.updateNumberOfCustomers(currentCustomerCount+1);
        roomRepository.save(room);

        // 토큰 발급
        AccessToken roomToken = createToken(roomId, kioskId);
        log.info("[createRoom] roomId: {}, kioskId: {}", roomId, kioskId);

        return roomToken;
    }

    /**
     * 고객 - 방 퇴장
     */
    @Transactional
    public void leaveRoom(String roomId) {
        Room room = getEntityById(roomRepository, roomId, RoomNotFoundException::new);

        int currentCustomerCount = room.getNumberOfCustomers();
        if (currentCustomerCount > 0) {
            room.updateNumberOfCustomers(currentCustomerCount - 1);
            roomRepository.save(room);
        }
        log.info("[leaveRoom] roomId: {}, new number of customers: {}", roomId, room.getNumberOfCustomers());
    }

    public void handleWebhook(String authHeader, String body) {
        WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            log.info("LiveKit Webhook: {}", event);
        } catch (Exception e) {
            log.error("Error validating webhook event: {}", e.getMessage());
            throw new RuntimeException("Failed to handle webhook", e);
        }
    }

    /**
     * roomID, userId롤 openVidu 접근을 위한 토큰 생성
     */
    private AccessToken createToken(String roomId, String userId) {
        AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        token.setName(roomId);
        token.setIdentity(userId);
        token.addGrants(new RoomJoin(true), new RoomName(roomId));
        return token;
    }

    /**
     * Room 메시지 송신 메서드
     *
     * @param room
     * @param participantName 송신자
     */
    private void sendMessageToRoom(LivekitModels.Room room, String participantName) throws IOException {
        LivekitModels.DataPacket.Kind kind = LivekitModels.DataPacket.Kind.RELIABLE;
        List<String> destinationIdentities = List.of();
        String jsonPayload = String.format("{\"participantName\": \"상담 요청: %s\"}", participantName);
        roomServiceClient.sendData(room.getName(), jsonPayload.getBytes(), kind, destinationIdentities).execute();
        log.info("Message sent to room: {}", room.getName());
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