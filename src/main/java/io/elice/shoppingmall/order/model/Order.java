package io.elice.shoppingmall.order.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_process")
    private String orderProcess;

    private String receiver;

    private String address;

    @Column(name = "delivery_process")
    private String deliveryProcess;

    private String request;

    private Long total_cost
}
