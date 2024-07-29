package com.example.pinokkio.api.category.dto.response;

import com.example.pinokkio.api.category.Category;
import com.example.pinokkio.common.response.GroupResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class GroupCategoryResponse extends GroupResponse<Category, CategoryResponse> {
    public GroupCategoryResponse(List<Category> categoryList) {
        super(categoryList, CategoryResponse::new);
    }
}
