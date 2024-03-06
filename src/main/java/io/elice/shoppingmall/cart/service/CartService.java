package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.dto.CreateCartDTO;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.repository.CartRepository;
import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createCart(Long id) {

        Optional<User> foundUser = userRepository.findById(id);
        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(id);

        if(foundUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다");
        }
        if(foundCart.isPresent()) {
            throw new IllegalArgumentException("이미 장바구니가 존재합니다!");
        }

        Cart newCart = Cart.builder()
                .user(foundUser.get())
                .build();

        cartRepository.save(newCart);
    }

    public Cart findCartById(Long cartId) {

        Optional<Cart> foundCart = cartRepository.findById(cartId);

        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다.");
        }

        return foundCart.get();
    }

    public Cart deleteCart(Long userId) {

        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(userId);

        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다.");
        }

        cartRepository.delete(foundCart.get());

        return null;
    }
}
