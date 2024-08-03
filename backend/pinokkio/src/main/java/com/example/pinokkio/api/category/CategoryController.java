package com.example.pinokkio.api.category;

import com.example.pinokkio.api.category.dto.request.CategoryRequest;
import com.example.pinokkio.api.category.dto.response.CategoryResponse;
import com.example.pinokkio.api.category.dto.response.GroupCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Category Controller", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 조회", description = "특정 포스의 모든 카테고리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
                    content = @Content(schema = @Schema(implementation = GroupCategoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_POS', 'ROLE_KIOSK')")
    @GetMapping({"/pos/{posId}/categories"})
    public ResponseEntity<?> getAllCategories(
            @PathVariable String posId) {
        List<Category> categoryList = categoryService.getGroupCategories(toUUID(posId));
        return new ResponseEntity<>(new GroupCategoryResponse(categoryList), HttpStatus.OK);
    }

    @Operation(summary = "카테고리 생성", description = "특정 포스의 카테고리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping({"/pos/{posId}/categories"})
    public ResponseEntity<?> makeCategory(
            @PathVariable String posId,
            @RequestBody CategoryRequest categoryRequest) {
        Category category = this.categoryService.createCategory(categoryRequest.getName(), toUUID(posId));
        return new ResponseEntity<>(new CategoryResponse(category), HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 삭제", description = "특정 포스의 카테고리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
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
