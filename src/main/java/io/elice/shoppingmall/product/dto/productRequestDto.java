package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class productRequestDto {
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
