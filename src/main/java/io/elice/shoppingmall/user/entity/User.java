package io.elice.shoppingmall.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.order.model.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String phoneNumber;

    private String postcode;

    private String address1;

    private String address2;

    private boolean isAdmin;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Cart cart;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonManagedReference
    private List<Orders> orders = new ArrayList<>();

}
