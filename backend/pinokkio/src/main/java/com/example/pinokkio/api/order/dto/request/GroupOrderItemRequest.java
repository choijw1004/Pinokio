package com.example.pinokkio.api.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupOrderItemRequest {
    private String customerId; //nullable
    private List<OrderItemRequest> orderItems = new ArrayList<>();
}
