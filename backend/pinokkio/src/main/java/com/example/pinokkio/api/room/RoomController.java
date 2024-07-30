package com.example.pinokkio.api.room;

import com.example.pinokkio.api.room.dto.request.RoomEnterRequest;
import com.example.pinokkio.api.room.dto.response.RoomResponse;
import io.livekit.server.AccessToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Room Controller", description = "화상 상담 관련 API")
public class RoomController {

    private final RoomService roomService;
    private final WebSocketService webSocketService;

    @Operation(summary = "상담 생성", description = "Teller의 경우 상담방 생성 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "화상 상담 생성 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "화상 상담 생성 실패",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}")
    public ResponseEntity<RoomResponse> createRoom(
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        log.info("Creating room for teller: {}", tellerId);
        AccessToken roomToken = roomService.createRoom(tellerId);
        return ResponseEntity.ok(new RoomResponse("Room created successfully", roomToken.toJwt()));
    }

    @Operation(summary = "상담 삭제", description = "Teller의 경우 상담방 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "화상 상담 삭제 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "화상 상담 삭제 실패",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @DeleteMapping("/teller/{tellerId}")
    public ResponseEntity<RoomResponse> deleteRoom(
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        log.info("Deleting room for teller: {}", tellerId);
        roomService.deleteRoom(tellerId);
        return ResponseEntity.ok(new RoomResponse("Room deleted successfully", null));
    }

    @Operation(summary = "상담 요청 수락", description = "Teller가 상담 요청을 수락하는 경우 Room ID를 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상담 요청 수락 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 토큰이 발급된 참가자",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "Room ID 전송 실패",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}/accept")
    public ResponseEntity<RoomResponse> acceptInvitation(
            @Validated @RequestBody RoomEnterRequest enterRequest,
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        log.info("Accepting invitation for room: {}, teller: {}, kiosk: {}", enterRequest.getRoomId(), tellerId, enterRequest.getUserId());
        if (webSocketService.isTokenIssued(enterRequest.getUserId())) {
            return ResponseEntity.badRequest().body(new RoomResponse("Token already issued for this participant", null));
        }
        String acceptRoomId = roomService.acceptInvitation(enterRequest.getRoomId(), tellerId, enterRequest.getUserId());
        try {
            webSocketService.sendRoomId(enterRequest.getUserId(), acceptRoomId);
            return ResponseEntity.ok(new RoomResponse("RoomId sent successfully", null));
        } catch (RuntimeException e) {
            log.error("Error sending roomId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomResponse("Error sending roomId", null));
        }
    }

    @Operation(summary = "상담 요청 거부", description = "Teller가 상담 요청을 거부하는 경우")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상담 요청 거부 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/reject")
    public ResponseEntity<RoomResponse> rejectInvitation(
            @Validated @RequestBody RoomEnterRequest roomEnterRequest) {
        log.info("Rejecting invitation for room: {}, teller: {}", roomEnterRequest.getRoomId(), roomEnterRequest.getUserId());
        roomService.rejectInvitation(roomEnterRequest.getRoomId(), roomEnterRequest.getUserId());
        return ResponseEntity.ok(new RoomResponse("Invitation rejected successfully", null));
    }

    @Operation(summary = "상담 요청", description = "Kiosk의 경우 생성된 모든 상담방에 상담 요청 전송 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상담 요청 전송 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "상담 요청 전송 실패",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/{kioskId}/request-enter")
    public ResponseEntity<RoomResponse> requestEnterRoom(
            @Parameter(description = "Kiosk ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String kioskId) {
        log.info("Requesting consultation for kiosk: {}", kioskId);
        try {
            roomService.messageAllRooms(kioskId);
            return ResponseEntity.ok(new RoomResponse("Consultation request sent to all rooms", null));
        } catch (RuntimeException e) {
            log.error("Error sending consultation request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomResponse("Failed to send consultation request", null));
        }
    }

    @Operation(summary = "상담 입장", description = "Kiosk가 화상 상담에 입장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상담 입장 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "상담 입장 실패",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/enter")
    public ResponseEntity<RoomResponse> enterRoom(
            @Validated @RequestBody RoomEnterRequest enterRequest) {
        log.info("Kiosk entering room: {}, kiosk: {}", enterRequest.getRoomId(), enterRequest.getUserId());
        AccessToken roomToken = roomService.enterRoom(enterRequest.getRoomId(), enterRequest.getUserId());
        return ResponseEntity.ok(new RoomResponse("Entered room successfully", roomToken.toJwt()));
    }

    @Operation(summary = "상담 퇴장", description = "Kiosk가 화상 상담에서 퇴장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상담 퇴장 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/{roomId}/leave")
    public ResponseEntity<RoomResponse> leaveRoom(
            @Parameter(description = "Room ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String roomId) {
        log.info("Kiosk leaving room: {}", roomId);
        roomService.leaveRoom(roomId);
        return ResponseEntity.ok(new RoomResponse("Left room successfully", null));
    }

    @Operation(summary = "Webhook 수신", description = "LiveKit에서 전송된 Webhook 이벤트를 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook 처리 성공",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "Webhook 처리 실패",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PostMapping("/livekit/webhook")
    public ResponseEntity<RoomResponse> receiveWebhook(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody String body) {
        log.info("Received webhook event");
        try {
            roomService.handleWebhook(authHeader, body);
            return ResponseEntity.ok(new RoomResponse("Webhook processed successfully", null));
        } catch (RuntimeException e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RoomResponse("Failed to process webhook", null));
        }
    }
}
