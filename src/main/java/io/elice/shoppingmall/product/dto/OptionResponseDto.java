package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponseDto {
    private Long optionId;
    private String content;
    private int price;
    private int stock;
    private Long productId;
}
