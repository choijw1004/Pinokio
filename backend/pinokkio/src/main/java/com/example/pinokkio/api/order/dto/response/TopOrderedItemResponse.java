package com.example.pinokkio.api.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopOrderedItemResponse {
    private String itemId;
    private String itemName;
    private int totalQuantity;
}
