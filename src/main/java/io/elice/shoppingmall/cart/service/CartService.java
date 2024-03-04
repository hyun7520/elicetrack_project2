package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.dto.CreateCartDTO;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public void createCart(CreateCartDTO createCartDTO) {

        // 아직 product 와 user 안 넣음
//        Cart cart = Cart.builder().cartCount(1).build();

//        cartRepository.save(cart);

    }

    public List<Cart> findCartByUserId(Long userId) {

//         return cartRepository.findCartByUserId(userId);
        return null;
    }

    @Transactional
    public void updateCartCount(Long cartId) throws Exception {

//        Cart findCart = cartRepository.findById(cartId).orElseThrow(() -> new Exception("값을 찾을 수 없습니다."));
//
//        findCart.setCartCount(findCart.getCartCount() + 1);
//
//        cartRepository.save(findCart);
    }

    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
