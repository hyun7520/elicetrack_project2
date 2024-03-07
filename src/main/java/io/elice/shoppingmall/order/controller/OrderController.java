package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.*;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.response.OrderResponse;
import io.elice.shoppingmall.order.service.OrderService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성 - 결제 버튼 클릭 시
    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String dateNow = date.format(now);
        orderRequestDto.setOrderDate(now);

        Orders order = orderService.createOrder(orderRequestDto);

        if(order == null) {
            return OrderResponse.responseBuilder(null, "사용자가 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        }

        return OrderResponse.responseBuilder(order, "주문이 정상적으로 생성되었습니다!", HttpStatus.CREATED);
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
    public OrderResponseDto getOrderAndDetails(@PathVariable("id") Long id) {
        Orders orders = orderService.getOrderById(id);

        return new OrderResponseDto(orders);
    }

    // 사용자 별로 주문 조회
    @GetMapping("/user/{id}")
    public Page<Orders> getOrderByUser(@PathVariable("id") Long id,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Orders> pagedOrders = orderService.getOrdersByUserId(id, pageRequest);

        return pagedOrders;
    }

    // 주문 정보 작성 페이지 - 수신자, 주소 등
    // 장바구니에서 받아온 정보를 표시하고 form을 채워넣은 후 주문하면 post를 통해 주문 테이블이 생성된다.
    @GetMapping("/order-form")
    public String orderForm(Model model,
                            // 장바구니에서 정보 받아오기 - GetMapping인데 사용해도 되나?
                            @RequestBody List<Product> products) {
        model.addAttribute("products", products);

        return null;
    }

    // 주문 수정
    // 사이트 고객에 의한 배달 주소, 수신자, 요청사항 의 수정
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id,
                                              @RequestBody OrderUpdateDto orderUpdateDto) {

        Orders order = orderService.updateOrder(id, orderUpdateDto);

        if(order == null) {
            OrderResponse.responseBuilder(null, "배송 중이거나 배송 완료된 물건은 수정이 불가능합니다!", HttpStatus.BAD_REQUEST);
        }

        return OrderResponse.responseBuilder(order, "수정이 완료되었습니다!", HttpStatus.OK);
    }

    // 주문 취소 - 사용자 기능
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Object> cancelOrder(@PathVariable("id") Long id,
                                              @RequestBody OrderUpdateDto orderUpdateDto) {

        Orders canceledOrder = orderService.cancelOrder(id, orderUpdateDto);
        if(canceledOrder == null) {
            return OrderResponse.responseBuilder(null, "주문 취소가 불가능합니다!", HttpStatus.NOT_FOUND);
        }

        return OrderResponse.responseBuilder(canceledOrder, "주문 취소가 완료되었습니다!", HttpStatus.OK);
    }

    // 주문 상태 설정 - 관리자 기능
    @PutMapping("/{id}/set-order")
    public ResponseEntity<Object> setOrderStatus(@PathVariable("id") Long id,
                                                 @RequestBody OrderManagerUpdateDto orderManagerUpdateDto) {

        Orders order = orderService.managerUpdateOrder(id, orderManagerUpdateDto);

        if(order != null) {
            return OrderResponse.responseBuilder(order, "수정이 완료되었습니다!", HttpStatus.OK);
        }

        return OrderResponse.responseBuilder(null, "존재하지 않는 주문입니다!", HttpStatus.NOT_FOUND);
    }

    // 주문 삭제 - 관리자권한
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {

        if (orderService.deleteOrder(id)){
            return OrderResponse.responseBuilder(null, "삭제가 완료 되었습니다!", HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
