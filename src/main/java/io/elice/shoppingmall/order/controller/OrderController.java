package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.dto.OrderResponseDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.service.OrderDetailService;
import io.elice.shoppingmall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    // 모든 주문 조회
    @GetMapping
    public Page<Orders> getAllOrders(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Orders> pagedOrders = orderService.getAllOrders(pageRequest);

//        return orderService.getAllOrders();
        return pagedOrders;
    }

    // 모든 주문 상세 내역 조회
    @GetMapping("/details")
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailService.getAllOrderDetails();
    }

    // 사용자 별 주문 상세 내역 조회
    @GetMapping("/details/{id}")
    public Page<OrderDetail> getOrderDetailsByUser(@PathVariable("id") Long id,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "3") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDetail> pagedOrderDetails = orderDetailService.getOrderDetailsByUser(id, pageRequest);

        return pagedOrderDetails;
    }

    // 아이디로 주문 조회
    @GetMapping("/{id}")
    public OrderResponseDto getOrderAndDetails(@PathVariable("id") Long id) {
        Orders orders = orderService.getOrderById(id);
        OrderResponseDto orderResponseDto = new OrderResponseDto(orders);

        return orderResponseDto;
    }

    // 주문 생성
    @PostMapping
    public void createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto);
    }

    // 주문 상세 내역 생성
    @PostMapping("/details")
    public void createOrderDetail(@RequestBody OrderDetailRequestDto orderDetailRequestDto) {
        orderDetailService.createOrderDetail(orderDetailRequestDto);
    }

    // 주문 수정
    @PutMapping("/{id}")
    public void updateOrder(@PathVariable("id") Long id,
                              @RequestBody OrderRequestDto orderRequestDto) {
        orderService.updateOrder(id, orderRequestDto);
    }

    // 주문 상세 내역 수정
    @PutMapping("/details/{id}")
    public void updateOrderDetail(@PathVariable("id") Long id,
                                    @RequestBody OrderDetailRequestDto orderDetailRequestDto) {
        orderDetailService.updateOrderDetail(id, orderDetailRequestDto);
    }

    // 주문 삭제
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }

    // 주문 상세 내역 삭제
    @DeleteMapping("/details/{id}")
    public void deleteOrderDetail(@PathVariable("id") Long id) {
        orderDetailService.deleteOrderDetail(id);
    }
}
