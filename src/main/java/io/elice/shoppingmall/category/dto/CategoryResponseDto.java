package io.elice.shoppingmall.category.dto;

import io.elice.shoppingmall.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String code;
    private Category parent;
    private List<Category> children;
}
