package com.example.pinokkio.api.item;

import com.example.pinokkio.api.category.Category;
import com.example.pinokkio.api.category.CategoryService;
import com.example.pinokkio.api.item.dto.request.ItemRequest;
import com.example.pinokkio.api.item.dto.request.UpdateItemRequest;
import com.example.pinokkio.api.item.dto.response.GroupItemResponse;
import com.example.pinokkio.api.item.dto.response.ItemResponse;
import com.example.pinokkio.api.item.image.ImageService;
import com.example.pinokkio.exception.notFound.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;
    private final CategoryService categoryService;

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
    @GetMapping("/pos/items")
    public ResponseEntity<GroupItemResponse> getAllItems() {
        List<Item> items = itemService.getGroupItems();
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 개별 아이템 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable String itemId) {
        Item item = itemService.getItem(itemId);
        return ResponseEntity.ok(new ItemResponse(item));
    }

    /**
     * 카테고리 아이템 목록 조회
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/items/{categoryId}")
    public ResponseEntity<GroupItemResponse> getItemsByCategory(@PathVariable String categoryId) {
        Category findCategory = categoryService.getCategory(UUID.fromString(categoryId));
        List<Item> items = itemService.getGroupItemsByCategory(findCategory);
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 키워드 접두사 검색
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @GetMapping("/pos/items/search")
    public ResponseEntity<GroupItemResponse> getItemsByKeyword(@RequestParam String keyword) {
        List<Item> items = itemService.getGroupItemsByKeyword(keyword);
        return ResponseEntity.ok(new GroupItemResponse(items));
    }

    /**
     * 개별 아이템 수정
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @PutMapping("/pos/items/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable String itemId,
            @Validated @ModelAttribute UpdateItemRequest updateItemRequest) {
        Item findItem = itemService.getItem(itemId);
        if (findItem.getItemImage() != null) {
            imageService.deleteImage(findItem.getItemImage());
        }
        if(updateItemRequest.getFile() != null) {
            imageService.uploadImage(updateItemRequest.getFile());
        }
        itemService.updateItem(itemId, updateItemRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 개별 아이템 삭제
     */
    @PreAuthorize("hasRole('ROLE_POS')")
    @DeleteMapping("/pos/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
