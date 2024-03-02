package io.elice.shoppingmall.cart.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.elice.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@Table(name = "cart")
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "created_at")
    private Date createdAt;

    // check_yn 대기

    // TODO product, user

    // 유저와 일대일 매핑
    @OneToOne(mappedBy = "cart")
    private User user;

    // 다대다 매핑은 쓰지 않는 것이 좋다
    // CartItem 엔티티를 새로 생성하여 일대다 다대일 관계로 제품과 간접적으로 다대일 관계가 되도록 설정
    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();

}
