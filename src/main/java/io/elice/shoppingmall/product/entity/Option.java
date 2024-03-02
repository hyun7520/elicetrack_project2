package io.elice.shoppingmall.product.entity;

import io.elice.shoppingmall.product.dto.OptionRequestDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "options")
public class Option {
    @Id
    @Column(name = "optionId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock; //재고 수량

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public Option(String content, int price, int stock, Product product) {
        this.content = content;
        this.price = price;
        this.stock = stock;
    }

    public void updateOption(OptionRequestDto optionRequestDto) {
        this.content = optionRequestDto.getContent();
        this.price = optionRequestDto.getPrice();
        this.stock = optionRequestDto.getStock();
    }
}
