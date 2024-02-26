package io.elice.shoppingmall.product.repository;

import io.elice.shoppingmall.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
