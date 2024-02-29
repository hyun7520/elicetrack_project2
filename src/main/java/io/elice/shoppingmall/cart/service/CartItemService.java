package io.elice.shoppingmall.cart.service;


import io.elice.shoppingmall.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    @Transactional
    public Product addItemToCart(Long id) {


    }
}
