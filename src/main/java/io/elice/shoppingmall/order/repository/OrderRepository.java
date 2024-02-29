package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findAll(Pageable pageable);

    Page<Orders> findAllByUser_Id(Long id, Pageable pageable);
}
