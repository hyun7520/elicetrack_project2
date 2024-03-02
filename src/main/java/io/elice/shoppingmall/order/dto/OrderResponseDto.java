package io.elice.shoppingmall.order.dto;

import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {

    private Date orderDate;
    private Date deliveryDate;
    private String orderProcess;
    private String receiver;
    private String address;
    private String deliveryProcess;
    private String request;
    private Long totalCost;

    private List<OrderDetail> orderDetails;

    public OrderResponseDto(Orders order) {

        this.orderDate = order.getOrderDate();
        this.deliveryDate = order.getDeliveryDate();
        this.orderProcess = String.valueOf(order.getOrderProcess());
        this.deliveryProcess = String.valueOf(order.getDeliveryProcess());
        this.receiver = order.getReceiver();
        this.address = order.getAddress();
        this.request = order.getRequest();
        this.totalCost = order.getTotalCost();

        this.orderDetails = order.getOrderDetails();
    }
}
