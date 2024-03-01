package io.elice.shoppingmall.order.service;

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
    public String createOrder(OrderRequestDto orderRequestDto) {

        Optional<User> foundUser = userRepository.findById(orderRequestDto.getUserId());

        if(!foundUser.isPresent()) {
            return "고객이 존재하지 않습니다!";
        }

        Orders order = Orders.builder()
                .user(foundUser.get())
                .orderDate(orderRequestDto.getOrderDate())
                .deliveryDate(orderRequestDto.getDeliveryDate())
                .receiver(orderRequestDto.getReceiver())
                .address(orderRequestDto.getAddress())
                .request(orderRequestDto.getRequest())
                .totalCost(orderRequestDto.getTotalCost())
                .build();
        orderRepository.save(order);

        return "주문이 정상적으로 처리되었습니다!";
    }

    // 전체 주문 조회
    public Page<Orders> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    // 아이디로 특정 주문 조회
    public Orders getOrderById(Long id) {
        Optional<Orders> order = orderRepository.findById(id);
        if(!order.isPresent()) {
            throw new IllegalArgumentException();
        }
        return order.get();
    }

    public Page<Orders> getOrdersByUserId(Long id, Pageable pageable) {
        Page<Orders> foundOrder = orderRepository.findAllByUser_Id(id, pageable);
        if(foundOrder.getTotalElements() == 0) {
            return null;
        }
        return foundOrder;
    }

    // 주문 수정
    @Transactional
    public Orders updateOrder(Long id, OrderUpdateDto orderUpdateDto) {
        
        if(!checkOrder(id)) {
            return null;
        }
        Optional<Orders> foundOrder = orderRepository.findById(id);
        Orders toUpdateOrder = foundOrder.get();
        
        // 배송 중일 경우 주소와 같은 정보는 수정이 불가능
        // boolean을 return 하도록 수정
        if(toUpdateOrder.getDeliveryProcess().equals("배송중")) {
            return null;
        }
        toUpdateOrder.updateOrder(orderUpdateDto);
        return orderRepository.save(toUpdateOrder);
    }

    @Transactional
    public String cancelOrder(Long id, OrderUpdateDto orderUpdateDto) {

        Optional<Orders> foundOrder = orderRepository.findById(id);

        if(!foundOrder.isPresent()) {
            return "주문이 존재하지 않습니다.";
        }
        Orders order = foundOrder.get();
        if(String.valueOf(order.getOrderProcess()).equals("canceled")) {
            return "주문이 이미 취소되었습니다!";
        }
        order.cancelOrder();

        return "주문 취소 완료!";
    }

    // 주문 삭제(관리자 권한만)
    @Transactional
    public boolean deleteOrder(Long id ) {

        if(!checkOrder(id)) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }

    // 수정, 삭제하고자하는 주문이 존재하는지 확인
    public boolean checkOrder(Long id) {
        Optional<Orders> foundOrder = orderRepository.findById(id);
        return foundOrder.isPresent();
    }
}
