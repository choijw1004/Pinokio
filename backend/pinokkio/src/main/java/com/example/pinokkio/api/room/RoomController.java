package com.example.pinokkio.api.room;

import com.example.pinokkio.api.room.dto.request.RoomEnterRequest;
import com.example.pinokkio.api.room.dto.request.RoomInfo;
import io.livekit.server.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = "*") // 추후 수정
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/meeting")
public class RoomController {
// TODO: DTO, 리턴타입, URL

    private final RoomService roomService;
    private final WebSocketService webSocketService;

    /**
     * 상담원 - 방 생성
     * ROLE_TELLER인 경우만 생성 가능
     */
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping(value = "/{tellerId}")
    public ResponseEntity<Map<String, String>> createRoom(@PathVariable String tellerId) {

        AccessToken roomToken = roomService.createRoom(tellerId);
        return ResponseEntity.ok(Map.of("token", roomToken.toJwt()));
    }

    /**
     * 상담원 - 방 퇴장
     * 방 퇴장과 동시에 방 삭제
     */
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @DeleteMapping(value = "/{tellerId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String tellerId) {

        roomService.deleteRoom(tellerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 상담원 - 고객의 상담 요청 승인 및 입장을 위한 방 정보 제공
     */
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/{tellerId}/accept")
    public ResponseEntity<Map<String, String>> acceptInvitation(@RequestBody RoomEnterRequest enterRequest, @PathVariable String tellerId) {
        String roomId = enterRequest.getRoomId();
        String kioskId = enterRequest.getUserId();

        if (roomId == null || kioskId == null || tellerId == null) {
            log.info("Missing roomId, kioskId, or tellerId");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing required parameters"));
        }

        // 입장 가능 여부 검증
        if (webSocketService.isTokenIssued(kioskId)) {
            return ResponseEntity.status(400).body(Map.of("error", "Token already issued for this participant"));
        }
        String acceptRoomId = roomService.acceptInvitation(roomId, tellerId, kioskId);

        // roomId 전송
        try {
            webSocketService.sendRoomId(kioskId, acceptRoomId);
            return ResponseEntity.ok(Map.of("message", "RoomId sent successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 상담원 - 고객의 상담 요청 거절
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/reject")
    public ResponseEntity<String> rejectInvitation(@RequestBody RoomInfo roomInfo) {
        String roomId = roomInfo.getRoomId();
        String tellerId = roomInfo.getTellerId();

        if (roomId == null || tellerId == null) {
            return ResponseEntity.badRequest().body("Missing roomId or tellerId");
        }

        roomService.rejectInvitation(roomId, tellerId);
        return ResponseEntity.ok("Invitation rejected for room: " + roomId);
    }

    /**
     * 고객 - 상담 요청 전송
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/enter/{kioskId}")
    public ResponseEntity<String> messageAllRooms(@PathVariable String kioskId) {
        if (kioskId == null) {
            return ResponseEntity.badRequest().body("Message and kioskId are required");
        }

        // 모든 방에 상담 요청 전송
        try {
            roomService.messageAllRooms(kioskId);
            return ResponseEntity.ok("Message sent to all rooms");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to send message to all rooms: " + e.getMessage());
        }
    }

    /**
     * 고객 - 입장 요청
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@RequestBody RoomEnterRequest enterRequest) {
        String roomId = enterRequest.getRoomId();
        String kioskId = enterRequest.getUserId();

        if (roomId == null || kioskId == null) {
            return ResponseEntity.badRequest().body("Missing roomId or kioskId");
        }

        AccessToken roomToken = roomService.enterRoom(roomId, kioskId);
        return ResponseEntity.ok(Map.of("token", roomToken.toJwt()));
    }

    /**
     * 고객 - 방 퇴장
     * Room 객체의 NumberOfCustomers 감소
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PutMapping(value = "/{roomId}")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId) {

        roomService.leaveRoom(roomId);
        return ResponseEntity.ok().build();
    }

    /**
     * WebHook 이벤트 수신
     */
    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        try {
            roomService.handleWebhook(authHeader, body);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to process webhook: " + e.getMessage());
        }
    }
}
