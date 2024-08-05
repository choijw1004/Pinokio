package com.example.pinokkio.api.customer;

import com.example.pinokkio.api.category.dto.response.GroupCategoryResponse;
import com.example.pinokkio.api.customer.dto.request.CustomerRegistrationEvent;
import com.example.pinokkio.api.customer.dto.request.CustomerRegistrationRequest;
import com.example.pinokkio.api.customer.sse.SSEService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Base64;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Controller", description = "고객 관련 API")
public class CustomerController {

    private final CustomerService customerService;
    private final SSEService sseService;

    @Operation(summary = "신규 고객 등록", description = "특정 포스에 신규 고객을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고객 등록 성공",
                    content = @Content(schema = @Schema(implementation = GroupCategoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationRequest request) {
        log.info("Received registration request: {}", request);
        byte[] faceEmbeddingDataBytes = Base64.getDecoder().decode(request.getFaceEmbeddingData());
        Customer newCustomer = customerService.saveCustomer(request.getCustomer(), faceEmbeddingDataBytes);
        return ResponseEntity.ok(newCustomer);

    }

    @GetMapping("/face-recognition-events")
    public SseEmitter subscribeToEvents() {
        return sseService.createEmitter();
    }

    // CustomerRegistrationEvent를 처리하는 이벤트 리스너 메서드입니다.
    @EventListener
    public void handleCustomerRegistrationEvent(CustomerRegistrationEvent event) {
        // 이벤트에 포함된 요청 정보로 고객 등록을 수행합니다.
        register(event.getRequest());
    }
}
