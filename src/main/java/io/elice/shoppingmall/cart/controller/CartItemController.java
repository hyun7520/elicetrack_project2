package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CartItemResponseDto;
import io.elice.shoppingmall.cart.dto.CartItemUpdateDto;
import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart/user/{id}/items")
public class CartItemController {

    private final CartItemService cartItemService;

    // 카트에 아이템 추가
    @PostMapping
    public CartItem addItemToCart(@PathVariable("id") Long id,
                                  @RequestParam("product") Long productId,
                                  @RequestParam("qty") int qty) {

        return cartItemService.addItemToCart(id, productId, qty);
    }

    @GetMapping
    public List<CartItemResponseDto> getAllItemsInCart(@PathVariable("id") Long id) {

        List<CartItemResponseDto> cartItems = cartItemService.getAllItemsInCart(id);

        return cartItems;
    }

    // 카트에 담긴 아이템의 수량을 수정
    @PutMapping("/{cartItemId}")
    public CartItem updateItemQuantity(@PathVariable("id") Long id,
                                       @PathVariable("id") Long cartItemId,
                                     @RequestBody CartItemUpdateDto cartItemUpdateDto) {

        return cartItemService.updateItemQuantity(id, cartItemId, cartItemUpdateDto);
    }


    // 선택된 아이템을 카트에서 모두 삭제
    @DeleteMapping
    public String deleteItemsFromCart(@PathVariable("id") Long id,
                                      @RequestBody String selectedItemIds) {

        JSONParser parser = new JSONParser();
        JSONArray param = null;

        try {
            param = (JSONArray) parser.parse(selectedItemIds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 받아온 json 스트링을 파싱하여 전달
        cartItemService.deleteAllSelected(id, param);

        return null;
    }
}
