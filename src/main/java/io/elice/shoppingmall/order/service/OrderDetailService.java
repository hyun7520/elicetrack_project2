package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderDetailUpdateDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.repository.OrderDetailRepository;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 주문 상세 생성
    @Transactional
    public OrderDetail createOrderDetail(Long id, OrderDetailRequestDto orderDetailRequestDto){

        Optional<Orders> foundOrder = orderRepository.findById(id);
        Optional<Product> foundProduct = productRepository.findById(orderDetailRequestDto.getProductId());

        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다!");
        }
        if(foundProduct.isEmpty()) {
            throw new IllegalArgumentException("제품이 존재하지 않습니다!");
        }
        Orders order = foundOrder.get();

        OrderDetail orderDetail = OrderDetail.builder()
                .product(foundProduct.get())
                .order(order)
                .quantity(orderDetailRequestDto.getQuantity())
                .productName(foundProduct.get().getProductName())
                .price(orderDetailRequestDto.getPrice())
                .build();

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        order.addOrderDetails(orderDetail);

        return savedOrderDetail;
    }

    // 사용자 별 주문 상세 조회
    public Page<OrderDetail> getOrderDetailsByOrder(Long id, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_id(id, pageable);
    }

    // 제품 수량 변경
    @Transactional
    public OrderDetail updateOrderDetail(Long id, Long detailId, OrderDetailUpdateDto orderDetailUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);

        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
        if(foundOrderDetail.isEmpty()) {
            throw new IllegalArgumentException("주문 상세내역이 존재하지 않습니다.");
        }

        Orders order = foundOrder.get();
        OrderDetail toUpdateOrderDetail = foundOrderDetail.get();
        OrderDetail updatedOrderDetail = null;
        if (order.getOrderDetails().contains(toUpdateOrderDetail)) {
            toUpdateOrderDetail.updateOrderDetail(orderDetailUpdateDto.getQuantity());
            updatedOrderDetail = orderDetailRepository.save(toUpdateOrderDetail);
        }
        return updatedOrderDetail;
    }


    // 상세 주문 삭제
    @Transactional
    public String deleteOrderDetail(Long orderId, Long detailId) {

        Optional<Orders> foundOrder = orderRepository.findById(orderId);
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);

        // 주문이 존재하는지, 삭제하고자하는 상세 내역이 존재하는지 확인
        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
        if(foundOrderDetail.isEmpty()) {
            throw new IllegalArgumentException("주문 상세내역이 존재하지 않습니다.");
        }

        orderDetailRepository.deleteById(detailId);
        return foundOrderDetail.get().getProduct().getProductName();
    }

    // 선택된 상세 내역 모두 삭제
    // 리스트로 받아온다.
    @Transactional
    public String deleteSelectedDetails(Long id, List<Long> selectedDetailIds) {

        Optional<Orders> foundOrder = orderRepository.findById(id);
        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다!");
        }
        if(selectedDetailIds.isEmpty()) {
            throw new IllegalArgumentException("삭제할 주문을 선택해주세요!");
        }

        orderRepository.deleteByIdIn(selectedDetailIds);

        return foundOrder.get().getReceiver();
    }
}
