package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByParentId(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category findByCode(String code) {
        return categoryRepository.findByCode(code).orElse(null);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("잘못된 접근입니다"));
    }
}
