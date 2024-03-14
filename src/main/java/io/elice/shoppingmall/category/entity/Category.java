package io.elice.shoppingmall.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.elice.shoppingmall.category.dto.CategoryRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @JoinColumn(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    @JoinColumn(name = "content")
    private String content;

    @Column(name = "image_url") // 이미지 경로 저장
    private String imageUrl;

    private String originalFileName;

    private String storedFileName;

    private long fileSize;

    @Builder
    public Category(String categoryName, Long parentId) {
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.imageUrl = imageUrl;
    }

    public void updateCategory(CategoryRequestDto categoryRequestDto){
        this.categoryName = categoryRequestDto.getCategoryName();
        this.parentId = categoryRequestDto.getParentId();
        this.content = categoryRequestDto.getContent();
        this.imageUrl = categoryRequestDto.getImageUrl();
    }
}
