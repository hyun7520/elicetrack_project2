package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
