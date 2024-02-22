package io.elice.shoppingmall.order.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.elice.shoppingmall.order.dto.OrderRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "order_process")
    private String orderProcess;

    @NotNull(message = "받는 분을 입력해주세요.")
    @Size(min = 2)
    private String receiver;

    @NotNull(message = "주소를 입력해주세요")
    private String address;

    @Column(name = "delivery_process")
    private String deliveryProcess;

    private String request = "안전하게 배송해주세요";

    @NotNull
    @Min(100)
    private Long totalCost;

    // 상세주문과 일대다 매핑
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 회원 정보와 다대일 매핑
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @Builder
    public Orders(Date orderDate, Date deliveryDate,
                 String orderProcess, String receiver,
                 String address, String deliveryProcess,
                 String request, Long totalCost) {
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderProcess = orderProcess;
        this.receiver = receiver;
        this.address = address;
        this.deliveryProcess = deliveryProcess;
        this.request = request;
        this.totalCost = totalCost;
    }

    public void updateOrder(OrderRequestDto orderRequestDto) {
        this.orderDate = orderRequestDto.getOrderDate();
        this.deliveryDate = orderRequestDto.getDeliveryDate();
        this.orderDate = orderRequestDto.getOrderDate();
        this.orderProcess = orderRequestDto.getOrderProcess();
        this.receiver = orderRequestDto.getReceiver();
        this.address = orderRequestDto.getAddress();
        this.deliveryProcess = orderRequestDto.getDeliveryProcess();
        this.request = orderRequestDto.getRequest();
        this.totalCost = orderRequestDto.getTotalCost();
    }
}
