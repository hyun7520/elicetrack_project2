package io.elice.shoppingmall.orderItem.repository;

import io.elice.shoppingmall.orderItem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
