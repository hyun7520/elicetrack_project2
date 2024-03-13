package io.elice.shoppingmall.cart.dto;

import io.elice.shoppingmall.cart.entity.Cart;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class CartResponseDto {

    private String userName;
    private Cart cart;
    private String message;
    private HttpStatus httpStatus;

    @Builder
    public CartResponseDto(String userName, Cart cart, String message, HttpStatus httpStatus) {
        this.userName = userName;
        this.cart = cart;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
