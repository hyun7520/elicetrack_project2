package io.elice.shoppingmall.orderItem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long product_id; //상품코드

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

    private int cnt;

    private String product_image;

    private int delivery_price;

    private LocalDate rdate;

    /*
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private OrderDetail orderDetail;

    @OneToMany(mappedBy = "product")
    private List<UserLike> userLikes;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<Option> options;

    @OneToMany(mappedBy = "product")
    private List<UserScrap> userScrap;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Cart carts;


    */

}
