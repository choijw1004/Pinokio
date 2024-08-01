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
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))

    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}")
    public ResponseEntity<?> createRoom(
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        AccessToken roomToken = roomService.createRoom(tellerId);
        return ResponseEntity.ok(new RoomResponse(roomToken.toJwt()));
    }

    @Operation(summary = "상담 삭제", description = "Teller의 경우 상담방 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT")
    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @DeleteMapping("/teller/{tellerId}")
    public ResponseEntity<?> deleteRoom(
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        roomService.deleteRoom(tellerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상담 요청 수락", description = "Teller가 상담 요청을 수락하는 경우 Room ID를 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_TELLER')")
    @PostMapping("/teller/{tellerId}/accept")
    public ResponseEntity<RoomResponse> acceptInvitation(
            @Validated @RequestBody RoomEnterRequest enterRequest,
            @Parameter(description = "Teller ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String tellerId) {
        if (webSocketService.isTokenIssued(enterRequest.getUserId())) {
            //TODO conflict 예외처리 하기
        }
        String acceptRoomId = roomService.acceptInvitation(enterRequest.getRoomId(), tellerId, enterRequest.getUserId());
        webSocketService.sendRoomId(enterRequest.getUserId(), acceptRoomId);
        //TODO bad input 예외처리 하기
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상담 요청 거부", description = "Teller가 상담 요청을 거부하는 경우")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT")
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/reject")
    public ResponseEntity<RoomResponse> rejectInvitation(
            @Validated @RequestBody RoomEnterRequest roomEnterRequest) {
        roomService.rejectInvitation(roomEnterRequest.getRoomId(), roomEnterRequest.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상담 요청", description = "Kiosk의 경우 생성된 모든 상담방에 상담 요청 전송 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PostMapping("/kiosk/{kioskId}/request-enter")
    public ResponseEntity<RoomResponse> requestEnterRoom(
            @Parameter(description = "Kiosk ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String kioskId) {
        roomService.messageAllRooms(kioskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상담 입장", description = "Kiosk가 화상 상담에 입장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PutMapping("/kiosk/enter")
    public ResponseEntity<RoomResponse> enterRoom(
            @Validated @RequestBody RoomEnterRequest enterRequest) {
        AccessToken roomToken = roomService.enterRoom(enterRequest.getRoomId(), enterRequest.getUserId());
        return ResponseEntity.ok(new RoomResponse(roomToken.toJwt()));
    }

    @Operation(summary = "상담 퇴장", description = "Kiosk가 화상 상담에서 퇴장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT")
    })
    @PreAuthorize("hasRole('ROLE_KIOSK')")
    @PutMapping("/kiosk/{roomId}/leave")
    public ResponseEntity<RoomResponse> leaveRoom(
            @Parameter(description = "Room ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String roomId) {
        roomService.leaveRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Webhook 수신", description = "LiveKit에서 전송된 Webhook 이벤트를 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT")
    })
    @PostMapping("/livekit/webhook")
    public ResponseEntity<RoomResponse> receiveWebhook(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody String body) {
        roomService.handleWebhook(authHeader, body);
        return ResponseEntity.noContent().build();
    }
}
