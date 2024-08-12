package com.example.pinokkio.api.order.dto.response;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GroupOrderItemResponse {
    private final UUID customerId;
    private final List<OrderItemResponse> orderItems;
    public GroupOrderItemResponse(UUID customerId, List<OrderItemResponse> orderItemResponses) {
        this.customerId = customerId;
        this.orderItems = orderItemResponses;
    }
}
