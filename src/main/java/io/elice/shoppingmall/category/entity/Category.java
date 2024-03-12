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
    @JoinColumn(name = "category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Category> children;

    @Column(nullable = false)
    @JoinColumn(name = "content")
    private String content;

    @Column(name = "image_url") // 이미지 경로 저장
    private String imageUrl;

    @Builder
    public Category(String name, Category parent) {
        this.categoryName = name;
        this.parent = parent;
        this.imageUrl = imageUrl;
    }

    public void updateCategory(CategoryRequestDto categoryRequestDto){
        this.categoryName = categoryRequestDto.getName();
        this.parent = categoryRequestDto.getParent();
        this.children = categoryRequestDto.getChildren();
        this.imageUrl = categoryRequestDto.getImageUrl();
    }
}
