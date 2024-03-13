package io.elice.shoppingmall.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderManagerUpdateDto {

    @NotBlank(message = "주문 상태는 비어있을 수 없습니다!")
    private String orderProcess;

    private String deliveryProcess;
}
