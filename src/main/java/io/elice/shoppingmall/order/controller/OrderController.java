package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderRequestDto;
import io.elice.shoppingmall.order.dto.OrderResponseDto;
import io.elice.shoppingmall.order.dto.OrderUpdateDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.order.service.OrderDetailService;
import io.elice.shoppingmall.order.service.OrderService;
import io.elice.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 모든 주문 조회
    @GetMapping
    public Page<Orders> getAllOrders(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Orders> pagedOrders = orderService.getAllOrders(pageRequest);

        return pagedOrders;
    }

    // 주문 아이디로 주문 조회
    @GetMapping("/{id}")
    public OrderResponseDto getOrderAndDetails(@PathVariable("id") Long id) {
        Orders orders = orderService.getOrderById(id);
        OrderResponseDto orderResponseDto = new OrderResponseDto(orders);

        return orderResponseDto;
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

    // 주문 생성 - 결제 버튼 클릭 시
    @PostMapping
    public String createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String dateNow = date.format(now);
        orderRequestDto.setOrderDate(now);

        // API 테스트를 위해 생성된 주문을 return하여 바로 확인이 가능하도록 했습니다.
        // 더 적합한 리턴값이 있을지 고민해보겠습니다.
        return orderService.createOrder(orderRequestDto);
    }

    // 주문 수정
    // 사이트 고객에 의한 배달 주소, 수신자, 요청사항 의 수정
    @PutMapping("/{id}")
    public Orders updateOrder(@PathVariable("id") Long id,
                              @RequestBody OrderUpdateDto orderUpdateDto) {

        return orderService.updateOrder(id, orderUpdateDto);
    }

    // 주문 삭제 - 관리자권한
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        if(orderService.deleteOrder(id)) {
            return "요청하신 주문이 삭제되었습니다.";
        }
        return "주문 삭제 중 오류가 발생했습니다. 다시 시도해주세요.";
    }

    // 주문 취소
    @PutMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id,
                              @RequestBody OrderUpdateDto orderUpdateDto) {

        return orderService.cancelOrder(id, orderUpdateDto);
    }

    // 주문 상태 설정 - 관리자 기능
    @PutMapping("/{id}/set-order")
    public String setOrderStatus(@PathVariable("id") Long id) {
        return null;
    }
}
