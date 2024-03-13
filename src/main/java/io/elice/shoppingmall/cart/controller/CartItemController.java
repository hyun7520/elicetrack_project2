package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CartItemResponseDto;
import io.elice.shoppingmall.cart.dto.CartItemUpdateDto;
import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.product.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartItemController {

    private final CartItemService cartItemService;

    // 카트에 아이템 추가
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<CartItemResponseDto> addItemToCart(@PathVariable("userId") Long userId,
                                  @RequestParam("product") @Min(1) Long productId,
                                  @RequestParam(value = "qty", required = false, defaultValue = "1") @Min(1) int qty) {


        CartItem createdCartItem = cartItemService.addItemToCart(userId, productId, qty);
        Product product = createdCartItem.getProduct();
        return ResponseEntity.ok(CartItemResponseDto.builder()
                .message(product.getProductName()+", "+createdCartItem.getAmount()+"개 가 장바구니에 담겼습니다!")
                .httpStatus(HttpStatus.CREATED)
                .build()
        );
    }

    @GetMapping("/user/{userId}/items")
    public ResponseEntity<List<CartItemResponseDto>> getAllItemsInCart(@PathVariable("userId") Long userId) {

        List<CartItemResponseDto> foundItems = cartItemService.getAllItemsInCart(userId);
        return ResponseEntity.ok(foundItems);
    }

    // 카트에 담긴 아이템의 수량을 수정
    @PutMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateItemQuantity(@PathVariable("userId") Long userId,
                                       @PathVariable("cartItemId") Long cartItemId,
                                     @RequestBody @Valid CartItemUpdateDto cartItemUpdateDto) {


        CartItem updateItem = cartItemService.updateItemQuantity(userId, cartItemId, cartItemUpdateDto);
        return ResponseEntity.ok(CartItemResponseDto.builder()
                .message("수정이 완료되었습니다.")
                .build());

    }

    // 선택된 아이템을 카트에서 모두 삭제
    @DeleteMapping("/user/{userId}/items")
    public ResponseEntity<CartItemResponseDto> deleteItemsFromCart(@PathVariable("userId") Long userId,
                                      @RequestBody @NotEmpty String selectedItemIds) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONArray param = null;
        param = (JSONArray) parser.parse(selectedItemIds);

        // 받아온 json 스트링을 파싱하여 전달
        String userName = cartItemService.deleteAllSelected(userId, param);
        return ResponseEntity.ok(CartItemResponseDto.builder()
                .message(userName + "님 삭제가 완료되었습니다.")
                .build());

    }
}
