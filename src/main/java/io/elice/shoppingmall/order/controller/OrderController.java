package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 모든 주문 조회
    @GetMapping
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 아이디로 주문 조회
    @GetMapping("/{id}")
    public Orders findOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    // 주문 생성
    @PostMapping
    public String createOrder(OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto);
        return "redirect:/orders";
    }

    // 주문 수정
    @PutMapping("/{id}")
    public String updateOrder(@PathVariable("id") Long id,
                              @ModelAttribute("dto") OrderRequestDto orderRequestDto) {
        orderService.updateOrder(id, orderRequestDto);
        return "redirect:/orders";
    }

    // 주문 삭제
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
