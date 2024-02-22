package io.elice.shoppingmall.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_detail")
@Getter
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    // 상품 아이디
    @NotNull
    @Column(name = "product_id")
    private Long productId;

    // 판매자
    @NotNull
    private String seller;

    // 상품 구매 개수
    @NotNull
    @Min(1)
    private Long quantity;

    // 상품 가격
    @NotNull
    @Min(100)
    private Long price;

    // 주문과 다대일 매핑
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
    
    // 상품과 1:1 매핑
//    @OneToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Builder
    public OrderDetail(Long productId, String seller, Long quantity, Long price) {
        this.productId = productId;
        this.seller = seller;
        this.quantity = quantity;
        this.price = price;
    }
}
