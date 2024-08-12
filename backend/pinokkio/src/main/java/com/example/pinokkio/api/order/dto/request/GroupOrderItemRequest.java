package com.example.pinokkio.api.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupOrderItemRequest {
    //nullable
    private UUID customerId;
    private List<OrderItemRequest> orderItems = new ArrayList<>();
}
