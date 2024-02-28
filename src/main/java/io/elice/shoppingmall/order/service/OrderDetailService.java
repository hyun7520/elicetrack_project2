package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.repository.OrderDetailRepository;
import io.elice.shoppingmall.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    // 주문 상세 생성
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailRequestDto orderDetailRequestDto){

        OrderDetail orderDetail = OrderDetail.builder()
                .productId(orderDetailRequestDto.getProductId())
                .quantity(orderDetailRequestDto.getQuantity())
                .price(orderDetailRequestDto.getPrice())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    // 전체 상세 주문 조회
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public Page<OrderDetail> getOrderDetailsByUser(Long id, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_id(id, pageable);
    }

    // 상세 주문 삭제
    @Transactional
    public boolean deleteOrderDetail(Long orderId, Long detailId) {
        // 주문이 존재하는지 확인
        Optional<Orders> foundOrder = orderRepository.findById(orderId);
        if(!foundOrder.isPresent()) {
            return false;
        }
        // 주문이 있을 경우 삭제하고자하는 상세 내역이 존재하는지 확인
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);
        if(!foundOrderDetail.isPresent()) {
            return false;
        }
        orderDetailRepository.deleteById(detailId);
        return true;
    }


    // 상세 주문 수정
//    @Transactional
//    public OrderDetail updateOrderDetail(Long id, OrderDetailRequestDto orderDetailRequestDto) {
//        Optional<OrderDetail> foundOrder = orderDetailRepository.findById(id);
//        if(!foundOrder.isPresent()) {
//            throw new IllegalArgumentException();
//        }
//        OrderDetail toUpdateOrderDetail = foundOrder.get();
//        toUpdateOrderDetail.updateOrderDetail(orderDetailRequestDto);
//        return orderDetailRepository.save(toUpdateOrderDetail);
//    }
}
