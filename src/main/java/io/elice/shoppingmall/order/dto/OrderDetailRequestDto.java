package io.elice.shoppingmall.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDto {

    @Min(value = 1, message = "제품은 비어있을 수 없습니다!")
    private Long productId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다!")
    private Long quantity;

    private String productName;

    @Min(value = 1000, message = "가격은 1000원 미만일 수 없습니다!")
    private Long price;
}
