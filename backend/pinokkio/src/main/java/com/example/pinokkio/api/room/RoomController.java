package com.example.pinokkio.api.room;

import io.livekit.server.*;
import jakarta.annotation.PostConstruct;
import livekit.LivekitModels;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*") // 추후 수정
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/meeting")
public class RoomController {
    //TODO: 불필요한 메서드 제거 및 추가, 서비스 로직 분리
    private final RoomService roomService;

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    private RoomServiceClient roomServiceClient;

    @PostConstruct
    public void init() {
        this.roomServiceClient = RoomServiceClient.create("http://localhost:7880/", LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
    }

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // WebSocket 핸들러에서 호출될 메서드
    public void handleWebSocketMessage(WebSocketSession session, TextMessage message) {
        try {
            JSONObject jsonMessage = new JSONObject(message.getPayload());
            if ("participantInfo".equals(jsonMessage.getString("type"))) {
                String participantName = jsonMessage.getString("participantName");
                sessions.put(participantName, session);
            }
        } catch (JSONException e) {
            log.info("[handleWebSocketMessage] ERROR:" + e.getMessage());
        }
    }

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
     * 상담원 - 고객의 상담 요청 승인
     */
    @PreAuthorize("hasAnyRole('ROLE_TELLER')")
    @PostMapping("/accept")
    public ResponseEntity<Map<String, String>> acceptInvitation(@RequestBody Map<String, String> params) {
        log.info("AcceptInvitation: {}", params);
        String roomName = params.get("roomName");
        String participantName = params.get("requestParticipantName");

        if (roomName == null || participantName == null) {
            log.info("Missing roomName or participantName");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing roomName or participantName"));
        }

        WebSocketSession session = sessions.get(participantName);
        if (session == null || !session.isOpen()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Session not found or closed"));
        }

        // 이미 토큰을 발급받은 참가자인지 확인
        if (session.getAttributes().containsKey("tokenIssued")) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Token already issued for this participant"));
        }

        // 승인받은 방 제목으로 토큰 생성
        AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        token.setName(roomName);
        token.setIdentity(participantName);
        token.addGrants(new RoomJoin(true), new RoomName(roomName));

        // 토큰 발급 표시
        session.getAttributes().put("tokenIssued", true);
        log.info("[AcceptInvitation] 토큰 발급 roomName: {} participantName: {}", roomName, participantName);
        return ResponseEntity.ok(Map.of("token", token.toJwt()));
    }

    /**
     * 상담원 - 고객의 상담 요청 거절
     */
    @PreAuthorize("hasAnyRole('ROLE_KIOSK')")
    @PostMapping("/reject")
    public ResponseEntity<String> rejectInvitation(@RequestBody Map<String, String> params) {
        String roomName = params.get("roomName");
        String participantName = params.get("participantName");

        if (roomName == null || participantName == null) {
            return ResponseEntity.badRequest().body("Missing roomName or participantName");
        }

        // Perform actions needed to reject the invitation
        return ResponseEntity.ok("Invitation rejected for room: " + roomName);
    }

    /**
     * 고객 - 상담 요청 전송
     */
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/messageAllRooms")
    public ResponseEntity<String> messageAllRooms(@RequestBody Map<String, String> params) {

        String message = params.get("message");
        String participantName = params.get("participantName");

        if (message == null || participantName == null) {
            return ResponseEntity.badRequest().body("Message and participantName are required");
        }

        log.info("Message: {}", message);
        log.info("ParticipantName: {}", participantName);

        try {
            Call<List<LivekitModels.Room>> roomListCall = roomServiceClient.listRooms();
            Response<List<LivekitModels.Room>> response = roomListCall.execute();

            if (!response.isSuccessful()) {
                return ResponseEntity.status(500).body("Failed to fetch rooms");
            }

            List<LivekitModels.Room> rooms = response.body();

            // 모든 방에 알림 전송
            for (LivekitModels.Room room : rooms) {
                LivekitModels.DataPacket.Kind kind = LivekitModels.DataPacket.Kind.RELIABLE;
                List<String> destinationIdentities = List.of(); // All participants

                String jsonPayload = "{\"message\": \"" + message + "\", \"participantName\": \"" + participantName + "\"}";
                Call<Void> call = roomServiceClient.sendData(room.getName(), jsonPayload.getBytes(), kind, destinationIdentities);
                Response<Void> sendResponse = call.execute();
                if (!sendResponse.isSuccessful()) {
                    System.err.println("Failed to send message to room: " + room.getName() + " with response: " + sendResponse.message());
                } else {
                    log.info("Message sent to room: {}", room.getName());
                }
            }
            return ResponseEntity.ok("Message sent to all rooms");
        } catch (Exception e) {
            log.info("[messageAllRooms] ERROR:{}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to send message to all rooms");
        }
    }

    /**
     * 상담원이 고객의 상담 요청 수락시 해당 정보를 바탕으로 고객에게 토큰 전송
     */
    @PreAuthorize("hasAnyRole('ROLE_KIOSK')")
    @PostMapping("/sendToken")
    public ResponseEntity<String> sendToken(@RequestBody Map<String, String> params) {
        String participantName = params.get("participantName");
        String token = params.get("token");

        if (participantName == null || token == null) {
            return ResponseEntity.badRequest().body("ParticipantName and token are required");
        }

        WebSocketSession session = sessions.get(participantName);
        log.info("[sendToken] session: " + session);
        if (session != null && session.isOpen()) {

            try {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("type", "token");
                jsonMessage.put("token", token);
                session.sendMessage(new TextMessage(jsonMessage.toString()));

                log.info("토큰 전송 성공!");
                return ResponseEntity.ok("Token sent");
            } catch (IOException | JSONException e) {
                log.info("[sendToken] ERROR:{}", e.getMessage());
                return ResponseEntity.status(500).body("Failed to send token");
            }
        } else {
            return ResponseEntity.status(404).body("Session not found or closed");
        }
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
            log.info("LiveKit Webhook: {}", event);
        } catch (Exception e) {
            System.err.println("Error validating webhook event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
