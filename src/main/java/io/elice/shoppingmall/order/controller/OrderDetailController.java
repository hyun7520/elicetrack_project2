package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderDetailUpdateDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // 특정 주문에 주문 상세 추가
    @PostMapping("/{id}")
    public String addOrderDetail(@PathVariable("id") Long id,
                                 @RequestBody OrderDetailRequestDto orderDetailRequestDto) {

        return orderDetailService.createOrderDetail(id, orderDetailRequestDto);
    }

    // 특정 주문의 주문 상세내역 조회
    @GetMapping("/{id}")
    public Page<OrderDetail> getOrderDetailsByUser(@PathVariable("id") Long id,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "3") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDetail> pagedOrderDetails = orderDetailService.getOrderDetailsByUser(id, pageRequest);

        return pagedOrderDetails;
    }

    @PutMapping("/{id}/{detailId}")
    public String updateOrderDetail(@PathVariable("id") Long id,
                                    @PathVariable("detailId") Long detailId,
                                    @RequestBody OrderDetailUpdateDto orderDetailUpdateDto) {
        return orderDetailService.updateOrderDetail(id, detailId, orderDetailUpdateDto);
    }

    // 주문 상세 내역 삭제
    @DeleteMapping("/{id}/{detailId}")
    public String deleteOrderDetail(@PathVariable("id") Long orderId, @PathVariable("detailId") Long detailId) {

        if(orderDetailService.deleteOrderDetail(orderId, detailId)) {
            return "주문 상세 내역이 삭제되었습니다.";
        }
        return "상품 삭제 중 오류가 발생했습니다. 다시 시도해주세요.";
    }

    // 선택된 주문 상세내역 일괄 삭제
    @DeleteMapping("/{id}")
    public String deleteSelectedOrderDetails(@PathVariable("id") Long id,
                                             @RequestBody List<Long> selectedDetailIds) {
        orderDetailService.deleteSelectedDetails(id, selectedDetailIds);
        
        // 주문 상세내역 전체 선택으로 삭제하면 상품 페이지나 메인페이지로 redirect 되도록 설정할 것
        // 살게 없는데 주문 테이블이 존재할 수 없기 때문에
        return "선택된 상품 삭제 성공";
    }
}
