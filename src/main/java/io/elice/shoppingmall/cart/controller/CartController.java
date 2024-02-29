package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CreateCartDTO;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
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

    // 장바구니 담긴 제품 정보를 주문 페이지로 전달 - 주문페이지에 model을 통해 정보를 출력
    // 이후 주문 도메인에서 추가정보를 입력하도록
    @GetMapping("/orders")
    public String cartToOrders(Model model,
                               @RequestBody List<Product> products) {

        model.addAttribute("products", products);

        return "order/order";
    }

    // 장바구니에 선택한 아이템 여러개 추가
    @PutMapping("/add/{userId}")
    public String addItemToCart(@RequestParam("item") List<Long> items) {

        for(Long i : items) {
            // cartService.addItemsToCart(i);
        }

        return null;
    }

    // 장바구니에서 선택한 제품 제거
    @DeleteMapping("/find/{userId}/delete-one")
    public String deleteItemFromCart(@PathVariable("userId") Long userId,
                                     @RequestParam("item") int item) {

        // cartService.deleteItemFromCart(item);

        // 삭제 완료 후 장바구니 페이지를 다시 로드
        return null;
    }

    // 장바구니에서 여러개를 선택 후 제거
    @DeleteMapping("/find/{userId}/delete-all")
    public String deleteSelectedItemsFromCart(@PathVariable("userId") Long userId,
                                              @RequestParam("items") List<Integer> items) {

        for(int i : items) {
            // 서비스 추가 예정
            // cartService.deleteItemsFromCart(i);
        }

        return null;
    }


}


