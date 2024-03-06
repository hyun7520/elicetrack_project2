package io.elice.shoppingmall.category.controller;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController{
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public Category save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @GetMapping("/code/{code}")
    public Category findByCode(@PathVariable String code) {
        return categoryService.findByCode(code);
    }

}
