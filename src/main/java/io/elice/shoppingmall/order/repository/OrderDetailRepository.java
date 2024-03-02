package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    Page<OrderDetail> findAllByOrder_id(Long id, Pageable pageable);
}
