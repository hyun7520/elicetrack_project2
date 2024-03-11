package io.elice.shoppingmall.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @Min(1)
    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
    
    // 디폴트 주문값으로 자동으로 채워줌
    private String orderProcess;

    @NotNull(message = "수신자는 Null 일 수 없습니다!")
    private String receiver;

    @NotBlank(message = "주소는 비어있을 수 없습니다!")
    @Pattern(regexp = "[A-Za-z0-9가-힣]*")
    private String address;

    private String deliveryProcess;

    private String request;

    @Min(value = 1000, message = "가격은 1000원 이하일 수 없습니다!")
    private Long totalCost;

}

