package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
<<<<<<< HEAD

=======
    Page<Review> findByProductProductId(Long id, Pageable pageable);
//    Page<Review> findAll(Pageable pageable);
>>>>>>> 8298897327af8b074ba46e14fd88ab220e6561fc
}
