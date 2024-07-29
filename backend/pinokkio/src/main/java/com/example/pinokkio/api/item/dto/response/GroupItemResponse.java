package com.example.pinokkio.api.item.dto.response;

import com.example.pinokkio.api.item.Item;
import com.example.pinokkio.common.response.GroupResponse;
import lombok.Getter;
import java.util.List;

@Getter
public class GroupItemResponse extends GroupResponse<Item, ItemResponse> {
    public GroupItemResponse(List<Item> itemList) {
        super(itemList, ItemResponse::new);
    }
}