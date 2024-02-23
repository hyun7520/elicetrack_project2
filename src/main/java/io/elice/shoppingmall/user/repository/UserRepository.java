package io.elice.shoppingmall.user.repository;

import io.elice.shoppingmall.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
