package com.example.pinokkio.api.item;

import com.example.pinokkio.api.category.Category;
import com.example.pinokkio.api.category.CategoryRepository;
import com.example.pinokkio.api.item.dto.request.ItemRequest;
import com.example.pinokkio.api.item.dto.request.UpdateItemRequest;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.exception.notFound.CategoryNotFoundException;
import com.example.pinokkio.exception.notFound.ItemNotFoundException;
import com.example.pinokkio.exception.notFound.PosNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final PosRepository posRepository;
    private final CategoryRepository categoryRepository;

    public Item getItem(String itemId) {
        return itemRepository
                .findById(UUID.fromString(itemId))
                .orElseThrow(()->new ItemNotFoundException(itemId));
    }

    public List<Item> getGroupItems() {
        return itemRepository.findAll();
    }

    public List<Item> getGroupItemsByCategory(Category category) {
        return itemRepository.findByCategory(category);
    }

    public List<Item> getGroupItemsByKeyword(String keyword) {
        return itemRepository.findItemsByKeyWord(keyword);
    }

    @Transactional
    public Item createItem(ItemRequest itemRequest, String imageURL) {
        Pos findPos = posRepository
                .findById(UUID.fromString(itemRequest.getPosId()))
                .orElseThrow(()->new PosNotFoundException(itemRequest.getPosId()));
        Category findCategory = categoryRepository.findById(UUID.fromString(itemRequest.getCategoryId()))
                .orElseThrow(() -> new CategoryNotFoundException(itemRequest.getCategoryId()));

        Item item = Item.builder()
                .pos(findPos)
                .category(findCategory)
                .price(itemRequest.getPrice())
                .amount(itemRequest.getAmount())
                .name(itemRequest.getName())
                .detail(itemRequest.getDetail())
                .itemImage(imageURL).build();
        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(String itemId, UpdateItemRequest updateItemRequest) {
        Item item = itemRepository
                .findById(UUID.fromString(itemId))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        item.updateAmount(updateItemRequest.getAmount());
        item.updatePrice(updateItemRequest.getPrice());
        item.updateName(updateItemRequest.getName());
        item.updateDetail(updateItemRequest.getDetail());
    }

    @Transactional
    public void deleteItem(String itemId) {
        Item item = itemRepository
                .findById(UUID.fromString(itemId))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        this.itemRepository.delete(item);
    }


}
