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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Item Controller", description = "아이템 관련 API")
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;

    @Operation(summary = "아이템 등록", description = "새로운 아이템을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(schema = @Schema(implementation = ItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping(value = "/pos/items", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> registerItem(
            @Parameter(description = "아이템 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = false) MultipartFile file,
            @Parameter(description = "아이템 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart("itemRequest") @Valid ItemRequest itemRequest) {

        String imageURL = imageService.uploadImage(file);
        Item item = itemService.createItem(itemRequest, imageURL);
        return new ResponseEntity<>(new ItemResponse(item), HttpStatus.CREATED);
    }

    @Operation(summary = "아이템 목록 조회", description = "특정 포스의 모든 아이템을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_POS', 'ROLE_KIOSK')")
    @GetMapping("/pos/items")
    public ResponseEntity<GroupItemResponse> getAllItems() {
        List<Item> items = itemService.getGroupItems();
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "아이템 조회", description = "특정 아이템을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ItemResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_POS', 'ROLE_KIOSK')")
    @GetMapping("/pos/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId) {
        Item item = itemService.getItem(toUUID(itemId));
        return ResponseEntity.ok(new ItemResponse(item));
    }

    @Operation(summary = "카테고리별 아이템 목록 조회", description = "특정 카테고리의 아이템 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_POS', 'ROLE_KIOSK')")
    @GetMapping("/pos/items/categories/{categoryId}")
    public ResponseEntity<GroupItemResponse> getItemsByCategory(
            @Parameter(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String categoryId) {
        List<Item> items = itemService.getGroupItemsByCategory(toUUID(categoryId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "키워드로 아이템 검색", description = "특정 포스의 키워드 접두사 기반 아이템 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = GroupItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_POS', 'ROLE_KIOSK')")
    @GetMapping("/pos/items/search")
    public ResponseEntity<GroupItemResponse> getItemsByKeyword(@ModelAttribute SearchItemRequest searchItemRequest) {
        List<Item> items = itemService.getGroupItemsByKeyword(searchItemRequest.getKeyWord());
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    @Operation(summary = "아이템 수정", description = "아이템 정보를 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping(value = "/pos/items/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId,
            @RequestPart(value = "file", required = false) MultipartFile file,  // MultipartFile을 따로 받음
            @RequestPart("updateItemRequest") @Validated UpdateItemRequest updateItemRequest) {  // DTO를 받음

        log.info("price={}", updateItemRequest.getPrice());
        log.info("amount={}", updateItemRequest.getAmount());
        log.info("file={}", file);

        itemService.updateItem(toUUID(itemId), updateItemRequest, file);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "아이템 삭제", description = "특정 아이템을 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_POS')")
    @DeleteMapping("/pos/items/{itemId}")
    public ResponseEntity<?> deleteItem(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId) {
        itemService.deleteItem(toUUID(itemId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "특정 아이템의 키오스크 표출 여부 TOGGLE", description = "키오스크 표출 여부 TOGGLE")
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/pos/items/{itemId}/toggle/screen")
    public ResponseEntity<?> toggleScreen(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId) {
        itemService.toggleScreenStatus(toUUID(itemId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "특정 아이템의 키오스크 품절 여부 TOGGLE", description = "키오스크 품절 여부 TOGGLE")
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/pos/items/{itemId}/toggle/sold-out")
    public ResponseEntity<?> toggleSoldOut(
            @Parameter(description = "아이템 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String itemId) {
        itemService.toggleSoldOutStatus(toUUID(itemId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * String to UUID
     */
    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
