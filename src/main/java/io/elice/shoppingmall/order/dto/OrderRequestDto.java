package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.model.Orders;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;
    private String orderProcess;
    private String receiver;
    private String address;
    private String deliveryProcess;
    private String request;
    private Long totalCost;

}

