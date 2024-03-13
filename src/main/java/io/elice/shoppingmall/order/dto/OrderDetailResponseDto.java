package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.model.OrderDetail;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class OrderDetailResponseDto {

    private OrderDetail orderDetail;
    private String message;
    private HttpStatus httpStatus;

    @Builder
    public OrderDetailResponseDto(OrderDetail orderDetail, String message, HttpStatus httpStatus) {
        this.orderDetail = orderDetail;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
