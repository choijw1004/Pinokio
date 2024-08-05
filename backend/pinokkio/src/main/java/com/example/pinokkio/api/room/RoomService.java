package com.example.pinokkio.api.room;

import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.room.dto.response.RoomResponse;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.common.utils.EntityUtils;
import com.example.pinokkio.config.jwt.JwtProvider;
import com.example.pinokkio.exception.domain.room.RoomAccessRestrictedException;
import com.example.pinokkio.exception.domain.room.RoomNotAvailableException;
import com.example.pinokkio.exception.domain.kiosk.KioskNotFoundException;
import com.example.pinokkio.exception.domain.room.RoomNotFoundException;
import com.example.pinokkio.exception.domain.teller.TellerNotFoundException;
import io.livekit.server.*;
import jakarta.annotation.PostConstruct;
import livekit.LivekitModels;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomService {
//TODO: 상담원, 고객 서비스 로직 분리

    // 상담원의 최대 응대 고객 수
    private final int MAX_CAPACITY = 3;

    private final RoomRepository roomRepository;
    private final TellerRepository tellerRepository;
    private final KioskRepository kioskRepository;
    private final JwtProvider jwtProvider;

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
    public RoomResponse createRoom() {
        Teller teller = getCurrentTeller();

        // 기존 방 존재 여부 확인 및 토큰 발급
        return roomRepository.findByTeller(teller)
                .map(room -> new RoomResponse(room.getRoomId().toString(),
                        createToken(room.getRoomId().toString(), teller.getId().toString()).toJwt()))
                .orElseGet(() -> {
                    Room newRoom = Room.builder()
                            .teller(teller)
                            .numberOfCustomers(0)
                            .build();
                    roomRepository.save(newRoom);

                    return new RoomResponse(newRoom.getRoomId().toString(),
                            createToken(newRoom.getRoomId().toString(), teller.getId().toString()).toJwt());
                });
    }

    /**
     * 상담원 - 상담원 퇴장 시 방 삭제
     */
    @Transactional
    public void deleteRoom() {
        Teller teller = getCurrentTeller();

        roomRepository.deleteByTeller(teller);
        log.info("[deleteRoom] complete for tellerId: {}",teller.getId());
    }

    /**
     * 상담원 - 상담 요청 수락시 고객의 방 접근을 위한 roomId 정보 제공
     */
    public String acceptInvitation(String roomId, String kioskId) {
        Teller teller = getCurrentTeller();

        try {
            EntityUtils.getEntityById(roomRepository, roomId, RoomNotFoundException::new);
            EntityUtils.getEntityById(kioskRepository, kioskId, KioskNotFoundException::new);
        } catch (Exception e) {
            log.info("[acceptInvitation] failed to find : {}",e.getMessage());
        }
        log.info("[acceptInvitation] roomId: {}, participantName: {}", roomId, teller.getId());
        return roomId;
    }

    private Teller getCurrentTeller() {
        String tellerEmail = jwtProvider.getCurrentUserEmail();
        return tellerRepository.findByEmail(tellerEmail)
                .orElseThrow(() -> new TellerNotFoundException(tellerEmail));
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
    public void messageAllRooms() {
        try {
            String kioskEmail = jwtProvider.getCurrentUserEmail();
            Kiosk kiosk = kioskRepository.findByEmail(kioskEmail)
                    .orElseThrow(() -> new KioskNotFoundException(kioskEmail));

            List<LivekitModels.Room> rooms = roomServiceClient.listRooms().execute().body();
            if (rooms == null || rooms.isEmpty()) throw new RoomAccessRestrictedException();
            rooms.forEach(room -> {
                try {
                    sendMessageToRoom(room, kiosk.getId().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            log.error("[messageAllRooms] Error: {}", e.getMessage());
            throw new RuntimeException("모든 방에 상담 요청 보내기 실패", e);
        }
    }

    /**
     * 고객 - 입장
     */
    @Transactional
    public AccessToken enterRoom(String roomId, String kioskId) {
        Kiosk kiosk = EntityUtils.getEntityById(kioskRepository, kioskId, KioskNotFoundException::new);
        Room room = EntityUtils.getEntityById(roomRepository, roomId, RoomNotFoundException::new);
        int currentCustomerCount = room.getNumberOfCustomers();
        if (room.getNumberOfCustomers() >= MAX_CAPACITY) {
            throw new RoomNotAvailableException(UUID.fromString(roomId));
        }
        room.updateNumberOfCustomers(currentCustomerCount+1);

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
        Room room = EntityUtils.getEntityById(roomRepository, roomId, RoomNotFoundException::new);

        int currentCustomerCount = room.getNumberOfCustomers();
        if (currentCustomerCount > 0) {
            room.updateNumberOfCustomers(currentCustomerCount - 1);
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

}