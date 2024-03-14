package io.elice.shoppingmall.category.service;

import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryFileService categoryFileService;

    public String uploadImage(MultipartFile file) throws IOException {
        // 카테고리 이미지를 저장하고 이미지의 URL을 반환
        List<Category> fileList = categoryFileService.saveCategoryImages(List.of(file));
        // 저장된 이미지의 URL을 반환합니다. 이 예제에서는 첫 번째 이미지만 반환합니다.
        return fileList.isEmpty() ? null : fileList.get(0).getStoredFileName();
    }

    // 모든 카테고리 반환
    public List<Category> getAllcategory(Long id) {
        return categoryRepository.findAll();
    }

    // 부모 카테고리 아이디로 카테고리 찾기
    public List<Category> findByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    // 부모 카테고리의 categoryId를 사용하여 하위 카테고리 반환
    public List<Category> findByParentCategoryId(Long parentCategoryId) {
        return categoryRepository.findByParentId(parentCategoryId);
    }

    // 부모 카테고리 아이디가 null인 모든 카테고리 반환
    public List<Category> getAllCategoriesWithNullParent() {
        return categoryRepository.findByParentIdIsNull();
    }

    // 카테고리 생성
    @Transactional
    public Category createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = Category.builder()
                .categoryName(categoryRequestDto.getCategoryName())
                .parentId(categoryRequestDto.getParentId())
                .content(categoryRequestDto.getContent())
                .imageUrl(categoryRequestDto.getImageUrl())
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
