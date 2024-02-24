package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CreateCartDTO;
import io.elice.shoppingmall.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public String createCart(@RequestBody CreateCartDTO createCartDTO) {
        cartService.createCart(createCartDTO);

        return null;
    }

    @GetMapping("/find/{userId}")
    public String findCartByUserId(@PathVariable Long userId) {
        cartService.findCartByUserId(userId);

        return null;
    }

    @PostMapping("/update/{cartId}")
    public String updateCartCount(@PathVariable Long cartId) throws Exception {

        cartService.updateCartCount(cartId);

        return null;
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);

        return null;
    }
}
