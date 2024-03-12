package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
public class OrderResponseDto {

    private Long id;
    private Date orderDate;
    private Date deliveryDate;
    private String orderProcess;
    private String receiver;
    private String address;
    private String deliveryProcess;
    private String request;
    private Long totalCost;

    private List<OrderDetail> orderDetails;
    private String userName;
    private String message;

    @Builder
    public OrderResponseDto(Long id, Orders order, String message, String userName) {

        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.deliveryDate = order.getDeliveryDate();
        this.orderProcess = String.valueOf(order.getOrderProcess());
        this.deliveryProcess = String.valueOf(order.getDeliveryProcess());
        this.receiver = order.getReceiver();
        this.address = order.getAddress();
        this.request = order.getRequest();
        this.totalCost = order.getTotalCost();
        this.orderDetails = order.getOrderDetails();
        this.message = message;
        this.userName = userName;
    }
}
