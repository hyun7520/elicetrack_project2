package io.elice.shoppingmall.product.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    public Option(String content, int price, int stock, Product productId) {
        this.content = content;
        this.price = price;
        this.stock = stock;
        this.productId = productId;
    }
}
