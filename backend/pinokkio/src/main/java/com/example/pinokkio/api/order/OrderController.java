package com.example.pinokkio.api.order;

import com.example.pinokkio.api.order.dto.request.GroupOrderItemRequest;
import com.example.pinokkio.api.order.dto.request.OrderDurationRequest;
import com.example.pinokkio.api.order.dto.response.GroupOrderItemResponse;
import com.example.pinokkio.api.order.dto.response.OrderDetailResponse;
import com.example.pinokkio.api.order.dto.response.OrderItemResponse;
import com.example.pinokkio.api.order.dto.response.TopOrderedItemResponse;
import com.example.pinokkio.api.pos.PosService;
import com.example.pinokkio.config.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Order Controller", description = "주문 관련 API")
public class OrderController {

    private final PosService posService;
    private final OrderService orderService;
    private final JwtProvider jwtProvider;


    @Operation(summary = "주문 정보 생성", description = "새로운 주문을 생성")
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(
            @RequestBody @Validated GroupOrderItemRequest groupOrderItemRequest) {
        List<OrderItemResponse> orderItemResponses = orderService.createOrder(
                        posService.getPosByEmail(jwtProvider.getCurrentUserEmail()).getId(),
                        groupOrderItemRequest
                )
                .getItems()
                .stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GroupOrderItemResponse(
                groupOrderItemRequest.getCustomerId(),
                orderItemResponses
        ));
    }

    @Operation(summary = "특정 기간의 주문 목록 조회", description = "특정 기간 동안의 포스의 주문 내역 상세 조회")
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/orders/duration")
    public ResponseEntity<List<OrderDetailResponse>> getOrdersByDuration(
            @ModelAttribute OrderDurationRequest orderDurationRequest) {
        List<OrderDetailResponse> orderDetails = orderService.getOrderItemsByDuration(orderDurationRequest);
        return ResponseEntity.ok(orderDetails);
    }

    @Operation(summary = "특정 고객의 최다 주문 아이템 조회", description = "특정 고객의 최다 주문 아이템 조회")
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/orders/customers/{customerId}/top-order")
    public ResponseEntity<?> getTopOrderItem(
            @PathVariable String customerId) {
        List<TopOrderedItemResponse> topItems = orderService.getTopOrderedItemByCustomerId(
                        toUUID(customerId),
                        posService.getPosByEmail(jwtProvider.getCurrentUserEmail()).getId()
                )
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        return ResponseEntity.ok(topItems);
    }


    @Operation(summary = "특정 고객의 최근 주문 아이템 조회", description = "특정 고객의 최근 주문 아이템 조회")
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/orders/customers/{customerId}/recent-items")
    public ResponseEntity<?> getRecentOrderItems(
            @PathVariable String customerId) {
        List<OrderItemResponse> orderItemResponses = orderService.getRecentOrderItemsByCustomerId(
                        toUUID(customerId),
                        posService.getPosByEmail(jwtProvider.getCurrentUserEmail()).getId()
                )
                .stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GroupOrderItemResponse(customerId, orderItemResponses));
    }

    @Operation(summary = "주문 상태 전환", description = "특정 주문의 상태를 전환합니다. [완료, 취소]")
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> toggleOrderStatus(@PathVariable UUID orderId) {
        orderService.toggleOrderStatus(
                orderId,
                posService.getPosByEmail(jwtProvider.getCurrentUserEmail()).getId()
        );
        return ResponseEntity.noContent().build(); // No Content 반환
    }

    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }

}
