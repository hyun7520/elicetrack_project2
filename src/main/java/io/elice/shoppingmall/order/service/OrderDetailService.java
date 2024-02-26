package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    // 주문 상세 생성
    @Transactional
    public void createOrderDetail(OrderDetailRequestDto orderDetailRequestDto){

        OrderDetail orderDetail = OrderDetail.builder()
                .productId(orderDetailRequestDto.getProductId())
                .quantity(orderDetailRequestDto.getQuantity())
                .price(orderDetailRequestDto.getPrice())
                .build();
        orderDetailRepository.save(orderDetail);
    }

    // 전체 상세 주문 조회
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public Page<OrderDetail> getOrderDetailsByUser(Long id, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_id(id, pageable);
    }

    // 상세 주문 수정
    @Transactional
    public void updateOrderDetail(Long id, OrderDetailRequestDto orderDetailRequestDto) {
        Optional<OrderDetail> foundOrder = orderDetailRepository.findById(id);
        if(!foundOrder.isPresent()) {
            throw new IllegalArgumentException();
        }
        OrderDetail toUpdateOrderDetail = foundOrder.get();
        toUpdateOrderDetail.updateOrderDetail(orderDetailRequestDto);
        orderDetailRepository.save(toUpdateOrderDetail);
    }

    // 상세 주문 삭제
    @Transactional
    public void deleteOrderDetail(Long id) {
        Optional<OrderDetail> foundOrder = orderDetailRepository.findById(id);
        if(!foundOrder.isPresent()) {
            throw new IllegalArgumentException();
        }
        orderDetailRepository.deleteById(id);
    }
}
