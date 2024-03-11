package io.elice.shoppingmall.cart.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponseDto {

    private Long id;
    private Date createdAt;
    private int amount;
    private String productName;
    private int price;
    private String brandName;

    private HttpStatus httpStatus;
    private String message;

    @Builder
    public CartItemResponseDto(Long id, Date createdAt, int amount, String productName,
                               int price, String brandName, HttpStatus httpStatus, String message) {
        this.id = id;
        this.createdAt = createdAt;
        this.amount = amount;
        this.productName = productName;
        this.price = price;
        this.brandName = brandName;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
