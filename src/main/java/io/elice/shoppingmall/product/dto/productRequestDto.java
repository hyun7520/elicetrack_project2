package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class productRequestDto {
    private String name;
    private int price;
    private String content;
    private int cnt;
    private String productImage;
    private int deliveryPrice;
    private LocalDate rdate;
    private Long categoryId;
}
