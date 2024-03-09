package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.dto.OrderManagerUpdateDto;
import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.dto.OrderUpdateDto;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 주문 생성
    @Transactional
    public Orders createOrder(OrderRequestDto orderRequestDto) {

        Optional<User> foundUser = userRepository.findById(orderRequestDto.getUserId());

        if(foundUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다!");
        }

        Orders order = Orders.builder()
                .user(foundUser.get())
                .orderDate(orderRequestDto.getOrderDate())
                .receiver(orderRequestDto.getReceiver())
                .address(orderRequestDto.getAddress())
                .request(orderRequestDto.getRequest())
                .totalCost(orderRequestDto.getTotalCost())
                .build();

        return orderRepository.save(order);
    }

    // 전체 주문 조회
    public Page<Orders> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    // 아이디로 특정 주문 조회
    public Orders getOrderById(Long id) {
        Optional<Orders> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
        return order.get();
    }

    // 사용자 아이디로 주문 조회
    public Page<Orders> getOrdersByUserId(Long id, Pageable pageable) {
        Page<Orders> foundOrder = orderRepository.findAllByUser_Id(id, pageable);
        if(foundOrder.getTotalElements() == 0) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
        return foundOrder;
    }

    // 주문 수정
    @Transactional
    public Orders updateOrder(Long id, OrderUpdateDto orderUpdateDto) {

        Orders toUpdateOrder = checkOrder(id);
        
        // 배송 중일 경우 주소와 같은 정보는 수정이 불가능
        // boolean을 return 하도록 수정
        if(String.valueOf(toUpdateOrder.getDeliveryProcess()).equals("shipping")) {
            throw new IllegalArgumentException("배송 중인 제품입니다!");
        }
        if(String.valueOf(toUpdateOrder.getDeliveryProcess()).equals("complete")) {
            throw new IllegalArgumentException("배송이 완료된 주문입니다!");
        }
        toUpdateOrder.updateOrder(orderUpdateDto);

        return orderRepository.save(toUpdateOrder);
    }

    // 주문 취소 - 사용자
    @Transactional
    public Orders cancelOrder(Long id, OrderUpdateDto orderUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);

        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다!");
        }
        Orders toCancelOrder = foundOrder.get();
        if(String.valueOf(toCancelOrder.getOrderProcess()).equals("canceled")) {
            throw new IllegalArgumentException("이미 취소된 주문입니다!");
        }
        toCancelOrder.cancelOrder();
        return orderRepository.save(toCancelOrder);
    }

    // 주문 수정 (결제 상태, 배송 상태) - 관리자
    @Transactional
    public Orders managerUpdateOrder(Long id, OrderManagerUpdateDto orderManagerUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);

        if(foundOrder.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다!");
        }
        Orders toUpdateOrder = foundOrder.get();
        toUpdateOrder.managerUpdateOrder(orderManagerUpdateDto);

        return orderRepository.save(toUpdateOrder);
    }

    // 주문 삭제(관리자 권한만)
    @Transactional
    public String deleteOrder(Long id ) {

        Orders toDeleteOrder = checkOrder(id);

        orderRepository.deleteById(id);

        return toDeleteOrder.getReceiver();
    }

    // 수정, 삭제하고자하는 주문이 존재하는지 확인
    public Orders checkOrder(Long id) {
        Optional<Orders> foundOrder = orderRepository.findById(id);
        if(foundOrder.isPresent()) {
            return foundOrder.get();
        }
        throw new IllegalArgumentException("주문이 존재하지 않습니다!");
    }
}
