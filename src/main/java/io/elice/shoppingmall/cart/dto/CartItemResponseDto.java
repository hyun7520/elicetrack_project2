package io.elice.shoppingmall.cart.dto;

import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartItemResponseDto {

    private Long id;
    private Date createdAt;
    private int amount;
    private String productName;
    private int price;
    private String brandName;

}
