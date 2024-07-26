package com.example.pinokkio.api.item.dto.response;

import com.example.pinokkio.api.item.Item;
import lombok.Getter;
import java.util.UUID;

@Getter
public class ItemResponse {

    private final String posId;
    private final String categoryId;
    private final int price;
    private final int amount;
    private final String name;
    private final String detail;
    private final String file;
    private final String isScreen;
    private final String isSoldOut;

    public ItemResponse(Item item) {
        this.posId = item.getPos().getId().toString();
        this.categoryId = item.getCategory().getId().toString();
        this.price = item.getPrice();
        this.amount = item.getAmount();
        this.name = item.getName();
        this.detail = item.getDetail();
        this.file = item.getItemImage();
        this.isScreen = item.getIsScreen().toString();
        this.isSoldOut = item.getIsSoldOut().toString();
    }
}
