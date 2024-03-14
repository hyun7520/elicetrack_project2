package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.order.service.OrderService;
import io.elice.shoppingmall.user.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    Date date = new Date();
    LocalDateTime localDateTime = LocalDateTime.now();

    private static Long userId = 1L;

    @Test
    @DisplayName("주문 생성")
    @Transactional
    @Order(1)
    public void createOrder() {

        //given
        OrderRequestDto orderRequestDto = new OrderRequestDto(userId, date, "received", "test receiver",
                "test address", "preparing", "none", 112323, 10000L);

        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .createdAt(localDateTime)
                .build();

        Orders foundOrder = Orders.builder()
                .user(user)
                .orderDate(orderRequestDto.getOrderDate())
                .receiver(orderRequestDto.getReceiver())
                .address(orderRequestDto.getAddress())
                .request(orderRequestDto.getRequest())
                .totalCost(orderRequestDto.getTotalCost())
                .postalCode(orderRequestDto.getPostalCode())
                .build();

        //when
        Orders savedOrder = orderRepository.save(foundOrder);

        //then
        assertEquals(1L, savedOrder.getUser().getId());
        assertEquals("test receiver", savedOrder.getReceiver());
        assertEquals(10000L, savedOrder.getTotalCost());
    }

    @Test
    @Transactional
    @DisplayName("사용자 별로 주문 조회하기")
    @Order(2)
    public void testGetService() {
//        int i = 0;
//        while(i < 5) {
//            orderRequestDto = new OrderRequestDto();
//            orderRequestDto.setUserId(1L);
//            orderRequestDto.setReceiver("test receiver");
//            orderRequestDto.setAddress("test address");
//            orderRequestDto.setTotalCost(10000L);
//
//            orderService.createOrder(orderRequestDto);
//
//            i++;
//        }
//
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        Page<Orders> foundOrders = orderService.getOrdersByUserId(1L, pageRequest);
//
//        assertNotNull(foundOrders);
//        assertEquals(5, foundOrders.getSize());
    }

}