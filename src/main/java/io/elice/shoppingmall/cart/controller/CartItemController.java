package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.entity.CartItem;
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
    
    // TODO RequestBody를 사용한 생성, 업데이트 방식에는 DTO를 사용하여 넣기

    // 카트에 담긴 모든 아이템 조회
    @GetMapping("/item/{id}")
    public List<CartItem> items(@PathVariable("id") Long id) {

        return cartItemService.getAllItems(id);
    }

    // 카트에 아이템 추가
    // postman에서 잘 추가가 됐는지 바로 확인하기 위해 리턴값은 Product로 설정
    @PostMapping("/item/{id}")
    public Product addItemToCart(@RequestBody Long id) {

        Product checkIfAdded = cartItemService.addItemToCart(id);

        return checkIfAdded;
    }

    // 카트에 담긴 아이템의 수량을 수정
    @PutMapping("/item/{id}")
    public String updateItemQuantity(// 수정할 cartItem 아이디와 변경할 수량 값을 받아오기
                                     @RequestBody Long id ) {

        cartItemService.updateItemQuantity(id);

        return null;
    }

    // 카트에서 아이템 삭제
    @DeleteMapping("/item/{id}")
    public String deleteItemFromCart(@RequestBody Long id) {

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
