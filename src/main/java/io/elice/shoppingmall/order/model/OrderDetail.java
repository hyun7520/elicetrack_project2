package io.elice.shoppingmall.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.elice.shoppingmall.product.entity.Product;
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
//    @NotNull
//    @Column(name = "product_id")
//    private Long productId;

    // 상품 구매 개수
    @NotNull
    @Min(1)
    private Long quantity;

    // 상품 가격
    @NotNull
    @Min(100)
    private Long price;

    // 주문과 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;
    
    // 상품과 1:1 매핑
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Builder
    public OrderDetail(Orders order, Product product, Long quantity, Long price) {
        //에러로 관련된 매핑 삭제
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public void updateOrderDetail(Long quantity) {
        this.quantity = quantity;
    }
}
