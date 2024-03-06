package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CartItemRequestDto;
import io.elice.shoppingmall.cart.dto.CartItemUpdateDto;
import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart/user/{id}/items")
public class CartItemController {

    private final CartItemService cartItemService;
    
    // TODO RequestBody를 사용한 생성, 업데이트 방식에는 DTO를 사용하여 넣기

    // 카트에 아이템 추가
    @PostMapping
    public CartItem addItemToCart(@PathVariable("id") Long id,
                                  @RequestParam("product") Long productId,
                                  @RequestParam("qty") int qty) {

        return cartItemService.addItemToCart(id, productId, qty);
    }

    // 카트에 담긴 아이템의 수량을 수정
    @PutMapping("/{cartItemId}")
    public CartItem updateItemQuantity(@PathVariable("id") Long id,
                                       @PathVariable("id") Long cartItemId,
                                     @RequestBody CartItemUpdateDto cartItemUpdateDto) {

        return cartItemService.updateItemQuantity(id, cartItemId, cartItemUpdateDto);
    }

    // 카트에서 아이템 삭제
    @DeleteMapping("/{cartItemId}")
    public String deleteItemFromCart(@PathVariable("id") Long id,
                                     @PathVariable("id") Long cartItemId) {

        cartItemService.deleteItemFromCart(id, cartItemId);

        return null;
    }

    // 선택된 아이템을 카트에서 모두 삭제
    @DeleteMapping
    public String deleteItemsFromCart(@PathVariable("id") Long id,
                                      @RequestBody List<Long> selectedItemIds) {

        // 리스트 형태로 삭제할 아이템의 아이디를 받아와 서비스단에 전달
        cartItemService.deleteAllSelected(id, selectedItemIds);

        return null;
    }
}
