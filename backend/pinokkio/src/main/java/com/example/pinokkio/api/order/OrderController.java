package com.example.pinokkio.api.order;

import com.example.pinokkio.api.customer.CustomerService;
import com.example.pinokkio.api.order.dto.request.GroupOrderItemRequest;
import com.example.pinokkio.api.order.dto.response.GroupOrderItemResponse;
import com.example.pinokkio.api.order.dto.response.OrderItemResponse;
import com.example.pinokkio.api.pos.PosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Order Controller", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 정보 생성", description = "새로운 주문을 생성")
    @PostMapping("/pos/{posId}/orders")
    public ResponseEntity<?> createOrder(
            @PathVariable String posId,
            @RequestBody @Validated GroupOrderItemRequest groupOrderItemRequest) {

        // 주문 생성
        Order order = orderService.createOrder(toUUID(posId), groupOrderItemRequest);

        // Stream을 사용하여 OrderItemResponse 리스트 생성
        List<OrderItemResponse> orderItemResponses = order.getItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());

        // GroupOrderItemResponse 생성자 호출
        GroupOrderItemResponse response = new GroupOrderItemResponse(
                groupOrderItemRequest.getCustomerId(),
                orderItemResponses
        );

        // 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
