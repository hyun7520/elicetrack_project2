package io.elice.shoppingmall.product.dto;

import io.elice.shoppingmall.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequestDto {
    @Min(value = 1)
    private Long optionId;

    @NotBlank(message = "설명문구는 필수입니다.")
    private String content;
    private int price;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int stock;

    @Min(value = 1)
    private Long productId;
}
