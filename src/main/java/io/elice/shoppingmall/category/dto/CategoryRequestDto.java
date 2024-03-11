package io.elice.shoppingmall.category.dto;

import io.elice.shoppingmall.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    private String name;
    private String code;
    private Category parent;

    private List<Category> children;
}
