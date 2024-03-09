package io.elice.shoppingmall.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemRequestDto {

    int amount;
    Long cartId;
    Long productId;
}
