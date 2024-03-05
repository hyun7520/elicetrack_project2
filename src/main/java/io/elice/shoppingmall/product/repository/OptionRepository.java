package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductProductId(Long productId);
}
