package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findAll(Pageable pageable);

    Page<Orders> findAllByUser_Id(Long id, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE o FROM order_detail o WHERE o.order_detail_id IN :ids")
    void deleteByIdIn(@Param("ids") List<Long> orderDetailIds);
}
