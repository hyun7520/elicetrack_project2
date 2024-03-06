package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductProductId(Long id, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}
