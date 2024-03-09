package io.elice.shoppingmall.cart.controller;

import io.elice.shoppingmall.cart.dto.CartResponseDto;
import io.elice.shoppingmall.cart.dto.CreateCartDTO;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/carts/user")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니에 아이템을 추가 및 삭제는 cartItemController에서
    // cart에서는 장바구니 비우기, 장바구니 아이템을 주문 페이지에 출력하기, 장바구니 조희 API를 구현

    // TO 박웅서
    // REST 설계 상 동사를 사용하면 좋지 않다고 알고 있고, "/create"는 POST와 중복되는 것 같아 삭제했습니다.
    // 아래의 GET 매핑에서도 같은 이유로 "/find"를 삭제했습니다.
    @PostMapping("/{userId}")
    public ResponseEntity<CartResponseDto> createCart(@PathVariable("userId") Long id) {

        try {
            Cart createdCart = cartService.createCart(id);
            return ResponseEntity.ok(CartResponseDto.builder()
                    .userName(createdCart.getUser().getNickname())
                    .message("장바구니 생성 완료!")
                    .httpStatus(HttpStatus.CREATED)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CartResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }
    
    // 장바구니 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> findCartByUserId(@PathVariable("userId") Long userId) {

        try {
            Cart foundCart = cartService.findCartByUserId(userId);
            return ResponseEntity.ok(CartResponseDto.builder()
                    .message("장바구니 조회완료!")
                    .cart(foundCart)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CartResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }

    // 로그아웃 시, 구매완료 시 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<CartResponseDto> deleteCart(@PathVariable("userId") Long userId) {

        try {
            String userName = cartService.deleteCartByUserId(userId);
            return ResponseEntity.ok(CartResponseDto.builder()
                    .message(userName+"님, 장바구니 삭제가 완료되었습니다.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CartResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }
}


