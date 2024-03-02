package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
