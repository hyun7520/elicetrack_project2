package io.elice.shoppingmall.product.dto;

import io.elice.shoppingmall.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.text.html.Option;

@Getter
@AllArgsConstructor
public class OptionResponseDto {
    private Long optionId;
    private String content;
    private int price;
    private int stock;
    private Long productId;
}
