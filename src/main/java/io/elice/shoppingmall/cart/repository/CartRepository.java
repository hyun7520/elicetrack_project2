package io.elice.shoppingmall.cart.repository;

import io.elice.shoppingmall.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 아직 유저 아이디가 없다.
//    List<Cart> findCartByUserId(Long userId);
}
