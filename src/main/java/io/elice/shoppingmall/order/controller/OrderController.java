package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.*;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성 - 결제 버튼 클릭 시
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String dateNow = date.format(now);
        orderRequestDto.setOrderDate(now);

        Orders order = orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(OrderResponseDto.builder()
                .id(order.getId())
                .order(order)
                .message("주문이 정상적으로 접수되었습니다!")
                .build());

    }

    // 모든 주문 조회
    @GetMapping
    public Page<Orders> getAllOrders(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return orderService.getAllOrders(pageRequest);
    }

    // 주문 아이디로 주문 조회
    @GetMapping("/{id}")
    public Orders getOrderAndDetails(@PathVariable("id") Long id) {

        return orderService.getOrderById(id);
    }

    // 사용자 별로 주문 조회
    @GetMapping("/user/{id}")
    public Page<Orders> getOrderByUser(@PathVariable("id") Long id,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return orderService.getOrdersByUserId(id, pageRequest);
    }

    // 주문 수정
    // 사이트 고객에 의한 배달 주소, 수신자, 요청사항 의 수정
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("id") Long id,
                                              @RequestBody @Valid OrderUpdateDto orderUpdateDto) {


        Orders order = orderService.updateOrder(id, orderUpdateDto);
        return ResponseEntity.ok(OrderResponseDto.builder()
                .order(order)
                .message("주문이 정상적으로 수정되었습니다!")
                .build());

    }

    // 주문 상태 설정 - 관리자 기능
    @PutMapping("/{id}/order-status")
    public ResponseEntity<OrderResponseDto> setOrderStatus(@PathVariable("id") Long id,
                                                           @RequestBody @Valid OrderManagerUpdateDto orderManagerUpdateDto) {


        Orders order = orderService.managerUpdateOrder(id, orderManagerUpdateDto);
        return ResponseEntity.ok(OrderResponseDto.builder()
                .order(order)
                .message("관리자님! 주문이 정상적으로 수정되었습니다!")
                .build());

    }

    // 주문 취소 - 사용자 기능
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable("id") Long id) {

        Orders canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(OrderResponseDto.builder()
                .order(canceledOrder)
                .message("주문이 정상적으로 취소되었습니다!")
                .build());
    }

    // 주문 삭제 - 관리자권한
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {

        String userName = orderService.deleteOrder(id);
        return ResponseEntity.ok(userName + "님에게 배송할 주문이 정상적으로 취소되었습니다!");

    }
}
