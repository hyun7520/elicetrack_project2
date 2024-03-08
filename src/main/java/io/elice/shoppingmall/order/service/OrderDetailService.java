package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderDetailUpdateDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.repository.OrderDetailRepository;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.order.response.OrderDetailResponse;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if(foundOrder.isPresent()) {
            Orders order = foundOrder.get();

            OrderDetail orderDetail = OrderDetail.builder()
                .product(foundProduct.get())
                .order(order)
                .quantity(orderDetailRequestDto.getQuantity())
                .price(orderDetailRequestDto.getPrice())
                .build();

            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            order.addOrderDetails(orderDetail);

            return savedOrderDetail;
        }
        return null;
    }

    // 사용자 별 주문 상세 조회
    public Page<OrderDetail> getOrderDetailsByUser(Long id, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_id(id, pageable);
    }

    // 제품 수량 변경
    @Transactional
    public OrderDetail updateOrderDetail(Long id, Long detailId, OrderDetailUpdateDto orderDetailUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);

        if (!foundOrder.isPresent() || !foundOrderDetail.isPresent()) {
            return null;
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

    // 선택된 상세 내역 모두 삭제
    // 리스트로 받아온다.
    @Transactional
    public boolean deleteSelectedDetails(Long id, List<Long> selectedDetailIds) {

        Optional<Orders> foundOrder = orderRepository.findById(id);
        if(!foundOrder.isPresent()) {
            return false;
        }
        if(selectedDetailIds.isEmpty())
            return false;

        orderRepository.deleteByIdIn(selectedDetailIds);

        return true;
    }
}
