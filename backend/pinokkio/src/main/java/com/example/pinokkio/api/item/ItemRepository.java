package com.example.pinokkio.api.item;

import com.example.pinokkio.api.category.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    /**
     * 입력받은 카테고리에 해당하는 아이템 리스트를 반환한다.
     * @param category 카테고리
     * @return 카테고리에 해당하는 아이템 리스트
     */
    List<Item> findByCategory(Category category);

    /**
     * 검색어를 접두사로 포함하는 아이템 리스트를 반환한다.
     * @param keyword 입력받는 검색어
     * @return 검색어로 시작하는 아이템 리스트
     */
    @Query("SELECT i FROM Item i WHERE i.name LIKE :keyword%")
    List<Item> findItemsByKeyWord(String keyword);
}
