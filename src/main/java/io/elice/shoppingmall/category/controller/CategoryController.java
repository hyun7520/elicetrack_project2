package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController{
    private final CategoryService categoryService;

    // 카테고리 모두 조회
    @GetMapping
    public List<Category> getAllcategory(@RequestParam(value = "parentId", required = false) Long parentId) {
        if (parentId == null) {
            return categoryService.getAllCategoriesWithNullParent();
        } else {
            return categoryService.getAllCategoriesWithNonNullParent();
        }
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
    @PostMapping
    public Category createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
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

    // 상위 카테고리의 하위 카테고리 조회
    @GetMapping("/{categoryId}/subcategories")
    public List<Category> getSubcategories(@PathVariable("categoryId") Long categoryId) {
        return categoryService.findByParentCategoryId(categoryId);
    }

    // 이미지 업로드
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadCategoryImage(@RequestParam("file") MultipartFile file) {
        try {
            // 이미지를 저장하고 이미지 URL을 반환
            String imageUrl = categoryService.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    // 이미지 반환
    @GetMapping("/get-image/{categoryId}")
    public String getImage(@PathVariable("categoryId") Long id) {
        Category category = categoryService.getCategoryById(id);
        String imgPath = category.getStoredFileName();
        log.info(imgPath);
        return "<img src=" + imgPath + ">";
    }
}
