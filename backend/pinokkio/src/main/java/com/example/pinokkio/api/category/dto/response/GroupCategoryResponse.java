package com.example.pinokkio.api.category.dto.response;

import com.example.pinokkio.api.category.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupCategoryResponse {
    private List<CategoryResponse> categoryList;

    public GroupCategoryResponse(List<Category> categoryList) {
        categoryList.stream().map(CategoryResponse::new).forEach((c) -> {
            this.categoryList.add(c);
        });
    }
}
