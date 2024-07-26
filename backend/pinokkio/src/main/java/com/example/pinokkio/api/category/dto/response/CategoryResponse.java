package com.example.pinokkio.api.category.dto.response;

import com.example.pinokkio.api.category.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponse {
    private final String id;
    private final String name;

    public CategoryResponse(Category category) {
        this.id = category.getId().toString();
        this.name = category.getName();
    }
}
