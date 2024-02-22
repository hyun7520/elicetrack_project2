package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
