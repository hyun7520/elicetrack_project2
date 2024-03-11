package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 모든 카테고리 반환
    public List<Category> getAllcategory() {
        return categoryRepository.findAll();
    }

    // 부모 id로 카테고리 찾기
    public List<Category> findByParentId(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    // 카테고리 생성
    @Transactional
    public Category createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = Category.builder()
                .categoryName(categoryRequestDto.getName())
                .parent(categoryRequestDto.getParent())
                .children(categoryRequestDto.getChildren())
                .build();
        return categoryRepository.save(category);
    }

    // id로 카테고리 반환
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("잘못된 접근입니다"));
    }

    // 카테고리 삭제
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    // 카테고리 수정
    @Transactional
    public Category updateById(Long id, CategoryRequestDto categoryRequestDto) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category existingCategory = category.get();
            existingCategory.updateCategory(categoryRequestDto);
            return categoryRepository.save(existingCategory);
        } else {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }
    }


}
