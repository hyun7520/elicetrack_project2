package io.elice.shoppingmall.cart.entity;


import io.elice.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private Integer cartCount;

    // check_yn 대기

    //TODO product, user
    @ManyToOne
    @JoinColumn(name = "product_id") // 외래 키로 사용할 칼럼명 지정
    private Product product;

}
