package com.example.pinokkio.api.order.dto.response;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupOrderItemResponse {
    private final String customerId;
    private final List<OrderItemResponse> orderItems;
    public GroupOrderItemResponse(String customerId, List<OrderItemResponse> orderItemResponses) {
        this.customerId = customerId;
        this.orderItems = orderItemResponses;
    }
}
