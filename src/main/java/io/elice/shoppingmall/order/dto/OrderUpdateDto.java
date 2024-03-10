package io.elice.shoppingmall.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {

    @NotNull(message = "수신자는 Null 일 수 없습니다!")
    private String receiver;

    @NotBlank(message = "주소는 비어있을 수 없습니다!")
    @Pattern(regexp = "[A-Za-z0-9가-힣]*")
    private String address;

    private String request;

    @NotBlank(message = "주문 상태는 비어있을 수 없습니다!")
    private String orderProcess;
}
