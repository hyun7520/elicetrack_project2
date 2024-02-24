package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class productResponseDto {
    private Long productId;
    private String productName;
    private int price;
    private String brandName;
    private String content;
    private int commentCount;
    private LocalDate createdDate;
    private String productImageUrl;
    private int deliveryPrice;
    private int averageScore;
    private int reviewCount;
    private int discountPrice;
    private Long categoryId;
}
