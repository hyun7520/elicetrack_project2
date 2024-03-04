package io.elice.shoppingmall.product.dto;

import io.elice.shoppingmall.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequestDto {
    private Long optionId;
    private String content;
    private int price;
    private int stock;
    private Long productId;
}
