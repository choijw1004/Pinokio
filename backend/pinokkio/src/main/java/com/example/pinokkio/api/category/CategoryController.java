package com.example.pinokkio.api.category;

import com.example.pinokkio.api.category.dto.request.CategoryRequest;
import com.example.pinokkio.api.category.dto.response.CategoryResponse;
import com.example.pinokkio.api.category.dto.response.GroupCategoryResponse;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 특정 포스의 모든 카테고리 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping({"/pos/{posId}/categories"})
    public ResponseEntity<?> getAllCategories(
            @PathVariable String posId) {
        List<Category> categoryList = categoryService.getGroupCategories(toUUID(posId));
        return new ResponseEntity<>(new GroupCategoryResponse(categoryList), HttpStatus.OK);
    }

    /**
     * 특정 포스의 카테고리 생성
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping({"/pos/{posId}/categories"})
    public ResponseEntity<?> makeCategory(
            @PathVariable String posId,
            @RequestBody CategoryRequest categoryRequest) {
        Category category = this.categoryService.createCategory(categoryRequest.getName(), toUUID(posId));
        return new ResponseEntity<>(new CategoryResponse(category), HttpStatus.CREATED);
    }

    /**
     * 카테고리 삭제
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @DeleteMapping({"pos/{posId}/categories/{categoryId}"})
    public ResponseEntity<?> deleteCategory(
            @PathVariable String posId,
            @PathVariable String categoryId) {
        categoryService.deleteCategory(toUUID(categoryId), toUUID(posId));
        return ResponseEntity.noContent().build();
    }

    /**
     * String to UUID
     */
    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }

}