package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<Review> findReviewsByProductId(Long productId);
}
