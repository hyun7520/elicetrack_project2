package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    // 주문 상세 생성
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

    // 상세 주문 수정
    public void updateOrderDetail() {

    }

    // 상세 주문 삭제
    public void deleteOrderDetail() {

    }
}
