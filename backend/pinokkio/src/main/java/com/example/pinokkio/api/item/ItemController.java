package com.example.pinokkio.api.item;

import com.example.pinokkio.api.item.dto.request.ItemRequest;
import com.example.pinokkio.api.item.dto.request.SearchItemRequest;
import com.example.pinokkio.api.item.dto.request.UpdateItemRequest;
import com.example.pinokkio.api.item.dto.response.GroupItemResponse;
import com.example.pinokkio.api.item.dto.response.ItemResponse;
import com.example.pinokkio.api.item.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Item Controller", description = "아이템 관련 API")
public class ItemController {

    //TODO: @PreAuthorize가 모든 메서드에 붙어 있음

    private final ItemService itemService;
    private final ImageService imageService;

    @Operation(summary = "아이템 등록", description = "새로운 아이템을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "아이템 등록 성공",
                    content = @Content(schema = @Schema(implementation = ItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping("/pos/items")
    public ResponseEntity<ItemResponse> registerItem(
            @Validated @ModelAttribute ItemRequest itemRequest) {
        String imageURL = imageService.uploadImage(itemRequest.getFile());
        Item item = itemService.createItem(itemRequest, imageURL);
        return new ResponseEntity<>(new ItemResponse(item), HttpStatus.CREATED);
    }

    @Operation(summary = "아이템 목록 조회", description = "특정 포스의 모든 아이템을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items")
    public ResponseEntity<GroupItemResponse> getAllItems(
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId) {
        List<Item> items = itemService.getGroupItems(toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "아이템 조회", description = "특정 아이템을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 조회 성공",
                    content = @Content(schema = @Schema(implementation = ItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId,
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId) {
        Item item = itemService.getItem(toUUID(itemId), toUUID(posId));
        return ResponseEntity.ok(new ItemResponse(item));
    }

    @Operation(summary = "카테고리별 아이템 목록 조회", description = "특정 카테고리의 아이템 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리별 아이템 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/{categoryId}")
    public ResponseEntity<GroupItemResponse> getItemsByCategory(
            @Parameter(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String categoryId,
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId) {
        List<Item> items = itemService.getGroupItemsByCategory(toUUID(categoryId), toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "키워드로 아이템 검색", description = "특정 포스의 키워드 접두사 기반 아이템 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "키워드 검색 성공",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/search")
    public ResponseEntity<GroupItemResponse> getItemsByKeyword(
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId,
            @RequestBody SearchItemRequest searchItemRequest) {
        List<Item> items = itemService.getGroupItemsByKeyword(searchItemRequest.getKeyWord(), toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "아이템 수정", description = "아이템 정보를 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "아이템 수정 성공"),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<Void> updateItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId,
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId,
            @Validated @ModelAttribute UpdateItemRequest updateItemRequest) {
        Item findItem = itemService.getItem(toUUID(itemId), toUUID(posId));
        if (findItem.getItemImage() != null) {
            imageService.deleteImage(findItem.getItemImage());
        }
        if (updateItemRequest.getFile() != null) {
            imageService.uploadImage(updateItemRequest.getFile());
        }
        itemService.updateItem(toUUID(itemId), toUUID(posId), updateItemRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아이템 삭제", description = "특정 아이템을 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "아이템 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @DeleteMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId,
            @Parameter(description = "포스 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String posId) {
        itemService.deleteItem(toUUID(itemId), toUUID(posId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * String to UUID
     */
    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
