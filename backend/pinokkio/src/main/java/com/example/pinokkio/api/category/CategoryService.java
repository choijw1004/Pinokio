package com.example.pinokkio.api.category;


import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    /**
     * 특정 포스의 카테고리 목록 조회
     */
    public List<Category> getGroupCategories() {
        UUID posId = userService.getCurrentPosId();
        return categoryRepository.findAllByPosId(posId);
    }

    /**
     * 특정 포스의 카테고리 생성
     */
    @Transactional
    public Category createCategory(String name) {
        Pos pos = userService.getCurrentPos();
        Category category = Category.builder()
                .name(name)
                .pos(pos)
                .build();
        return categoryRepository.save(category);
    }

    /**
     * 특정 포스의 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(UUID categoryId) {
        UUID posId = userService.getCurrentPosId();
        //소유권 검증
        validateCategory(categoryId, posId);
        log.info("category deleted: " + categoryId);
        categoryRepository.deleteByPosIdAndCategoryId(categoryId, posId);
    }

    /**
     * 카테고리 검증 함수
     * 해당 카테고리가 입력받은 포스의 카테고리인지 검증한다.
     *
     * @param categoryId 카테고리 식별자
     */
    public void validateCategory(UUID categoryId, UUID posId) {
        if (!categoryRepository.existsByPosIdAndCategoryId(posId, categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리가 해당 포스에 존재하지 않습니다.");
        }
    }

}