package com.example.pinokkio.api.item;


import com.example.pinokkio.api.item.dto.request.ItemRequest;
import com.example.pinokkio.api.item.dto.request.SearchItemRequest;
import com.example.pinokkio.api.item.dto.request.UpdateItemRequest;
import com.example.pinokkio.api.item.dto.response.GroupItemResponse;
import com.example.pinokkio.api.item.dto.response.ItemResponse;
import com.example.pinokkio.api.item.image.ImageService;
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
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;
    /**
     * 개별 아이템 생성
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @PostMapping({"/pos/items"})
    public ResponseEntity<?> registerItem(@Validated @ModelAttribute ItemRequest itemRequest) {
        String imageURL = imageService.uploadImage(itemRequest.getFile());
        Item item = itemService.createItem(itemRequest, imageURL);
        return new ResponseEntity<>(new ItemResponse(item), HttpStatus.CREATED);
    }

    /**
     * 전체 아이템 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items")
    public ResponseEntity<GroupItemResponse> getAllItems(
            @PathVariable String posId
    ) {
        List<Item> items = itemService.getGroupItems(toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 개별 아이템 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(
            @PathVariable String itemId,
            @PathVariable String posId) {
        Item item = itemService.getItem(toUUID(itemId), toUUID(posId));
        return ResponseEntity.ok(new ItemResponse(item));
    }

    /**
     * 카테고리 아이템 목록 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/{categoryId}")
    public ResponseEntity<GroupItemResponse> getItemsByCategory(
            @PathVariable String categoryId,
            @PathVariable String posId) {
        List<Item> items = itemService.getGroupItemsByCategory(toUUID(categoryId), toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 키워드 접두사 검색
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/{posId}/items/search")
    public ResponseEntity<GroupItemResponse> getItemsByKeyword(
            @PathVariable String posId,
            @RequestBody SearchItemRequest searchItemRequest) {
        List<Item> items = itemService.getGroupItemsByKeyword(searchItemRequest.getKeyWord(), toUUID(posId));
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 개별 아이템 수정
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable String itemId,
            @PathVariable String posId,
            @Validated @ModelAttribute UpdateItemRequest updateItemRequest) {
        Item findItem = itemService.getItem(toUUID(itemId), toUUID(posId));
        if (findItem.getItemImage() != null) {
            imageService.deleteImage(findItem.getItemImage());
        }
        if(updateItemRequest.getFile() != null) {
            imageService.uploadImage(updateItemRequest.getFile());
        }
        itemService.updateItem(toUUID(itemId), toUUID(posId), updateItemRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 개별 아이템 삭제
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @DeleteMapping("/pos/{posId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable String itemId,
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
