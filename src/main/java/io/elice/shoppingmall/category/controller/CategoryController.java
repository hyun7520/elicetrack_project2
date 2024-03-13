package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController{
    private final CategoryService categoryService;

//    @Value("${image.upload.path}")
//    private String imageUploadPath;

    // 카테고리 모두 조회
    @GetMapping
    public List<Category> getAllcategory(@RequestParam(value = "parent", required = false) Long parentId) {
        if (parentId == null) {
            return categoryService.getAllCategoriesWithNullParent();
        } else {
            return categoryService.findByParentId(parentId);
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

    // 상위 카테고리의 하위 카테고리 조회
    @GetMapping("/{categoryId}/subcategories")
    public List<Category> getSubcategories(@PathVariable("categoryId") Long categoryId) {
        return categoryService.findByParentCategoryId(categoryId);
    }

//    // 이미지 업로드
//    @PostMapping("/upload-image")
//    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
//        try {
//            String imageName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
//            File targetFile = new File(imageUploadPath + imageName);
//            image.transferTo(targetFile);
//            String imageUrl = "http://your-domain.com/images/" + imageName;
//            return ResponseEntity.ok(imageUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중에 오류가 발생했습니다.");
//        }
//    }
}
