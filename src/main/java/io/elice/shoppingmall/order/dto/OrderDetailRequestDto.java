package io.elice.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDto {
    ;
    private Long productId;
    private Long quantity;
    private Long price;
}
