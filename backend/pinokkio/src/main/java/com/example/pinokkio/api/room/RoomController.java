package com.example.pinokkio.api.room;

import io.livekit.server.AccessToken;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*") // 추후 수정
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    /**
     * Token 생성
     */
    private AccessToken getToken() {
        return new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
    }

    /**
     * 상담원 - 방 생성
     * ROLE_TELLER인 경우만 생성 가능
     */
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping(value = "/{tellerId}")
    public ResponseEntity<Map<String, String>> createRoom(@PathVariable String tellerId) {

        AccessToken roomToken = roomService.createRoom(getToken(), tellerId);
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
     * 고객 - Room 입장 토큰 생성
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/{kioskId}")
    public ResponseEntity<Map<String, String>> enterRoom(@PathVariable String kioskId) {

        AccessToken roomToken = roomService.enterRoom(getToken(), kioskId);
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


    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            System.out.println("LiveKit Webhook: " + event.toString());
        } catch (Exception e) {
            System.err.println("Error validating webhook event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
