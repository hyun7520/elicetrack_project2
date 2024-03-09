package io.elice.shoppingmall.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false)
    private String CategoryName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Category> children;

    @Builder
    public Category(String name, Category parent) {
        this.CategoryName = name;
        this.parent = parent;
    }

    public void updateCategory(CategoryRequestDto categoryRequestDto){
        this.CategoryName = categoryRequestDto.getName();
        this.parent = categoryRequestDto.getParent();
        this.children = categoryRequestDto.getChildren();
    }
}
