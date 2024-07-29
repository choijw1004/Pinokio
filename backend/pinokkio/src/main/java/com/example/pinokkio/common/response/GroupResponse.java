package com.example.pinokkio.common.response;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class GroupResponse<T, R> {
    private List<R> responseList;

    public GroupResponse(List<T> entityList, Function<T, R> mapper) {
        this.responseList = entityList.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
