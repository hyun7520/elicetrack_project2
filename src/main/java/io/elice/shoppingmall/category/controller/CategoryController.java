package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController{
    private final CategoryService categoryService;

    // 카테고리 모두 조회
    @GetMapping
    public List<Category> getAllcategory() {
        return categoryService.getAllcategory();
    }

    // id로 카테고리 모두 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long id) {
        Category category = categoryService.getCategoryById(id);

        if (category != null){
            return ResponseEntity.ok(category);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    // 카테고리 생성
    @PostMapping("/{categoryId}")
    public Category createCategory(@PathVariable("categoryId") Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public void deleteById(@PathVariable("categoryId") Long id) {
        categoryService.deleteById(id);
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public Category updateById(@PathVariable("categoryId") Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateById(id, categoryRequestDto);
    }

    // 카테고리 이미지 업로드
//    @PostMapping("/{categoryId}/image")
//    public Category uploadCategoryImage(@PathVariable("categoryId") Long id, @RequestParam("image_url") String imageUrl) {
//        return categoryService.uploadCategoryImage(id, imageUrl);
//    }

}
