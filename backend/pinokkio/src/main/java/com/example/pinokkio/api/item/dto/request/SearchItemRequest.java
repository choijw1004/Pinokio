package com.example.pinokkio.api.item.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchItemRequest {
    private String keyWord;
}
