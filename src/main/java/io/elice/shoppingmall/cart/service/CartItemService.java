package io.elice.shoppingmall.cart.service;


import io.elice.shoppingmall.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    @Transactional
    public Product addItemToCart(Long id) {

        return null;
    }

    public void deleteItemFromCart(Long id) {
    }

    public void deleteAllSelected(List<Long> selectedItemIds) {
    }
}
