package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class productResponseDto {
    private Long productId;
    private String name;
    private int price;
    private String content;
    private int cnt;
    private String productImage;
    private int deliveryPrice;
    private LocalDate rdate;
    private Long categoryId;
}
