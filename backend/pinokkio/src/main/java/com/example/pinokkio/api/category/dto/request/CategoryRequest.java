package com.example.pinokkio.api.category.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CategoryRequest {
    private final String name;
}