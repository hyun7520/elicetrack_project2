package io.elice.shoppingmall.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.dto.OrderUpdateDto;
import io.elice.shoppingmall.user.entity.User;
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
    @Enumerated(EnumType.STRING)
    private OrderProcess orderProcess;

    public enum OrderProcess {
        received, confirmed
    }

    @Column(name = "delivery_process")
    @Enumerated(EnumType.STRING)
    private DeliveryProcess deliveryProcess;

    public enum DeliveryProcess {
        preparing, shipping, complete
    }

    @NotNull(message = "받는 분을 입력해주세요.")
    @Size(min = 2)
    private String receiver;

    @NotNull(message = "주소를 입력해주세요")
    private String address;

    private String request = "안전하게 배송해주세요";

    @NotNull
    @Min(100)
    private Long totalCost;

    // 상세주문과 일대다 매핑
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 회원 정보와 다대일 매핑
    @ManyToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @Builder
    public Orders(Date orderDate, Date deliveryDate,
                 String address, String receiver,
                 String request, Long totalCost) {
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderProcess = OrderProcess.received;
        this.deliveryProcess = DeliveryProcess.preparing;
        this.receiver = receiver;
        this.address = address;
        this.request = request;
        this.totalCost = totalCost;
    }

    public void updateOrder(OrderUpdateDto orderUpdateDto) {

        this.receiver = orderUpdateDto.getReceiver();
        this.address = orderUpdateDto.getAddress();
        this.request = orderUpdateDto.getRequest();

    }

}
