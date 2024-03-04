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

import java.util.Iterator;
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
    public ResponseEntity<Object> createOrderDetail(Long id, OrderDetailRequestDto orderDetailRequestDto){

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

            return OrderDetailResponse.responseBuilder(savedOrderDetail, "상세 주문이 생성되었습니다!", HttpStatus.CREATED);
        }
        return OrderDetailResponse.responseBuilder(null, "주문이 존재하지 않습니다!", HttpStatus.NOT_FOUND);
    }

    // 전체 상세 주문 조회
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    // 사용자 별 주문 상세 조회
    public Page<OrderDetail> getOrderDetailsByUser(Long id, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_id(id, pageable);
    }

    // 제품 수량 변경
    public ResponseEntity<Object> updateOrderDetail(Long id, Long detailId, OrderDetailUpdateDto orderDetailUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);

        if (!foundOrder.isPresent() || !foundOrderDetail.isPresent()) {
            return OrderDetailResponse.responseBuilder(null, "변경하고자 하는 제품임 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        }
        Orders order = foundOrder.get();
        OrderDetail toUpdateOrderDetail = foundOrderDetail.get();
        OrderDetail updatedOrderDetail = null;
        if (order.getOrderDetails().contains(toUpdateOrderDetail)) {
            toUpdateOrderDetail.updateOrderDetail(orderDetailUpdateDto.getQuantity());
            updatedOrderDetail = orderDetailRepository.save(toUpdateOrderDetail);
        }
        return OrderDetailResponse.responseBuilder(updatedOrderDetail, "수량이 변경되었습니다!", HttpStatus.OK);
    }


    // 상세 주문 삭제
    @Transactional
    public ResponseEntity<Object> deleteOrderDetail(Long orderId, Long detailId) {
        // 주문이 존재하는지 확인
        Optional<Orders> foundOrder = orderRepository.findById(orderId);
        if(!foundOrder.isPresent()) {
            return OrderDetailResponse.responseBuilder(null, "주문이 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        }
        // 주문이 있을 경우 삭제하고자하는 상세 내역이 존재하는지 확인
        Optional<OrderDetail> foundOrderDetail = orderDetailRepository.findById(detailId);
        if(!foundOrderDetail.isPresent()) {
            return OrderDetailResponse.responseBuilder(null, "삭제하고자 하는 제품이 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        }
        orderDetailRepository.deleteById(detailId);
        return OrderDetailResponse.responseBuilder(null, "제품이 정상적으로 삭제되었습니다!", HttpStatus.OK);
    }

    // 선택된 상세 내역 모두 삭제
    // 리스트로 받아온다.
    @Transactional
    public ResponseEntity<Object> deleteSelectedDetails(Long id, List<Long> selectedDetailIds) {

        Long tempId;

        Optional<Orders> foundOrder = orderRepository.findById(id);
        if(!foundOrder.isPresent()) {
            return OrderDetailResponse.responseBuilder(null, "주문이 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        }
        if(selectedDetailIds.isEmpty())
            return OrderDetailResponse.responseBuilder(null, "삭제하고자 하는 제품이 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        orderRepository.deleteByIdIn(selectedDetailIds);

        return OrderDetailResponse.responseBuilder(null, "제품이 정상적으로 삭제되었습니다!", HttpStatus.OK);
    }
}
