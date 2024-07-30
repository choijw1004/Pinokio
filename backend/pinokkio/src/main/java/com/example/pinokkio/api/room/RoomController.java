package com.example.pinokkio.api.room;

import com.example.pinokkio.api.room.dto.request.RoomEnterRequest;
import com.example.pinokkio.api.room.dto.request.RoomInfo;
import com.example.pinokkio.api.room.dto.response.MeetingResponse;
import io.livekit.server.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // 추후 수정 예정
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/meeting")
public class RoomController {

    private final RoomService roomService;
    private final WebSocketService webSocketService;

    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}")
    public ResponseEntity<MeetingResponse> createRoom(@PathVariable String tellerId) {
        log.info("Creating room for teller: {}", tellerId);
        AccessToken roomToken = roomService.createRoom(tellerId);
        return ResponseEntity.ok(new MeetingResponse("Room created successfully", roomToken.toJwt()));
    }

    @PreAuthorize("hasRole('ROLE_TELLER')")
    @DeleteMapping("/teller/{tellerId}")
    public ResponseEntity<MeetingResponse> deleteRoom(@PathVariable String tellerId) {
        log.info("Deleting room for teller: {}", tellerId);
        roomService.deleteRoom(tellerId);
        return ResponseEntity.ok(new MeetingResponse("Room deleted successfully", null));
    }

    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}/accept")
    public ResponseEntity<MeetingResponse> acceptInvitation(@Validated @RequestBody RoomEnterRequest enterRequest, @PathVariable String tellerId) {
        log.info("Accepting invitation for room: {}, teller: {}, kiosk: {}", enterRequest.getRoomId(), tellerId, enterRequest.getUserId());
        if (webSocketService.isTokenIssued(enterRequest.getUserId())) {
            return ResponseEntity.badRequest().body(new MeetingResponse("Token already issued for this participant", null));
        }
        String acceptRoomId = roomService.acceptInvitation(enterRequest.getRoomId(), tellerId, enterRequest.getUserId());
        try {
            webSocketService.sendRoomId(enterRequest.getUserId(), acceptRoomId);
            return ResponseEntity.ok(new MeetingResponse("RoomId sent successfully", null));
        } catch (RuntimeException e) {
            log.error("Error sending roomId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MeetingResponse("Error sending roomId", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/reject")
    public ResponseEntity<MeetingResponse> rejectInvitation(@Validated @RequestBody RoomInfo roomInfo) {
        log.info("Rejecting invitation for room: {}, teller: {}", roomInfo.getRoomId(), roomInfo.getTellerId());
        roomService.rejectInvitation(roomInfo.getRoomId(), roomInfo.getTellerId());
        return ResponseEntity.ok(new MeetingResponse("Invitation rejected successfully", null));
    }

    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/{kioskId}/request-consultation")
    public ResponseEntity<MeetingResponse> requestConsultation(@PathVariable String kioskId) {
        log.info("Requesting consultation for kiosk: {}", kioskId);
        try {
            roomService.messageAllRooms(kioskId);
            return ResponseEntity.ok(new MeetingResponse("Consultation request sent to all rooms", null));
        } catch (RuntimeException e) {
            log.error("Error sending consultation request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MeetingResponse("Failed to send consultation request", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/enter")
    public ResponseEntity<MeetingResponse> enterRoom(@Validated @RequestBody RoomEnterRequest enterRequest) {
        log.info("Kiosk entering room: {}, kiosk: {}", enterRequest.getRoomId(), enterRequest.getUserId());
        AccessToken roomToken = roomService.enterRoom(enterRequest.getRoomId(), enterRequest.getUserId());
        return ResponseEntity.ok(new MeetingResponse("Entered room successfully", roomToken.toJwt()));
    }

    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/{roomId}/leave")
    public ResponseEntity<MeetingResponse> leaveRoom(@PathVariable String roomId) {
        log.info("Kiosk leaving room: {}", roomId);
        roomService.leaveRoom(roomId);
        return ResponseEntity.ok(new MeetingResponse("Left room successfully", null));
    }

    @PostMapping("/livekit/webhook")
    public ResponseEntity<MeetingResponse> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        log.info("Received webhook event");
        try {
            roomService.handleWebhook(authHeader, body);
            return ResponseEntity.ok(new MeetingResponse("Webhook processed successfully", null));
        } catch (RuntimeException e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MeetingResponse("Failed to process webhook", null));
        }
    }
}