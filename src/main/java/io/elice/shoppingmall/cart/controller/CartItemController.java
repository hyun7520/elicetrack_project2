package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    // 카트에 아이템 추가
    // postman에서 잘 추가가 됐는지 바로 확인하기 위해 리턴값은 Product로 설정
    @PutMapping("/item")
    public Product addItemToCart(@RequestParam("id") Long id) {

        Product checkIfAdded = cartItemService.addItemToCart(id);

        return checkIfAdded;
    }

    // 카트에서 아이템 삭제
    @DeleteMapping("/item/{id}")
    public String deleteItemFromCart(@PathVariable("id") Long id) {

        cartItemService.deleteItemFromCart(id);

        return null;
    }

    // 선택된 아이템을 카트에서 모두 삭제
    @DeleteMapping("/item")
    public String deleteItemsFromCart(@RequestBody List<Long> selectedItemIds) {

        // 리스트 형태로 삭제할 아이템의 아이디를 받아와 서비스단에 전달
        cartItemService.deleteAllSelected(selectedItemIds);

        return null;
    }

}
