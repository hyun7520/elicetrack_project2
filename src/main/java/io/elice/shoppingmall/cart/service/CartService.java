package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.repository.CartRepository;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart createCart(Long userId) {

        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다");
        }

        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(userId);
        if(foundCart.isPresent()) {
            return foundCart.get();
        }

        Cart newCart = Cart.builder()
                .user(foundUser.get())
                .build();

        return cartRepository.save(newCart);
    }

    public Cart findCartByUserId(Long userId) {

        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(userId);

        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다.");
        }

        return foundCart.get();
    }

    @Transactional
    public String deleteCartByUserId(Long userId) {

        Optional<User> foundUser = userRepository.findById(userId);
        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(userId);

        if(foundUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다!");
        }
        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다!");
        }

        cartRepository.delete(foundCart.get());
        return foundUser.get().getNickname();
    }
}
