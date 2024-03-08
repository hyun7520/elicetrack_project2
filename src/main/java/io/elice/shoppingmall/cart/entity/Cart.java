package io.elice.shoppingmall.cart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.elice.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import jdk.jfr.Frequency;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Table(name = "cart")
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;

    // check_yn 대기

    // TODO product, user

    // 유저와 일대일 매핑

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    // CartItem 엔티티를 새로 생성하여 일대다 다대일 관계로 제품과 간접적으로 다대일 관계가 되도록 설정
    @OneToMany(mappedBy = "cart", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();

    @Builder
    public Cart(User user) {
        this.user = user;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    public void emptyCart() {
        this.cartItems.clear();
    }

}
