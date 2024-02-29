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

    // TO 박웅서
    // REST 설계 상 동사를 사용하면 좋지 않다고 알고 있고, "/create"는 POST와 중복되는 것 같아 삭제했습니다.
    // 아래의 GET 매핑에서도 같은 이유로 "/find"를 삭제했습니다.
    @PostMapping
    public String createCart(@RequestBody CreateCartDTO createCartDTO) {
        cartService.createCart(createCartDTO);

        return null;
    }

    @GetMapping("/{userId}")
    public String findCartByUserId(@PathVariable Long userId) {
        cartService.findCartByUserId(userId);

        return null;
    }

    // TO 박웅서
    // 제 파트에서는 PUT을 사용해 업데이트를 진행했습니다. 통일성을 위해 팀원과 논의 후 제 파트를 수정하거나 해당 부분을 수정하겠습니다.
    @PostMapping("/update/{cartId}")
    public String updateCartCount(@PathVariable Long cartId) throws Exception {

        cartService.updateCartCount(cartId);

        return null;
    }

    // 유저 당 하나의 카트를 가지기 때문에 삭제하기보다는 로그아웃 시,
    // 또는 구매완료 시 내부 데이터만 지우는 방식으로 수정하는게 어떨까요?
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

    // 장바구니에 상품페이지에서 선택한 아이템 여러개 추가
    @PutMapping("/{cartId}/items")
    public String addItemToCart(@RequestParam("item") List<Long> items) {

        for(Long i : items) {
            // cartService.addItemsToCart(i);
        }

        return null;
    }

}


