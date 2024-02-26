package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 아직 유저 아이디가 없다.
    List<Cart> findCartByUserId(Long userId);
}
