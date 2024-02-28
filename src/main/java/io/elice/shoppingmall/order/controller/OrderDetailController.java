package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // 특정 주문의 주문 상세 조회
    @GetMapping("/{id}")
    public Page<OrderDetail> getOrderDetailsByUser(@PathVariable("id") Long id,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "3") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDetail> pagedOrderDetails = orderDetailService.getOrderDetailsByUser(id, pageRequest);

        return pagedOrderDetails;
    }

    // 주문 상세 내역 생성
    @PostMapping("/{id}")
    public OrderDetail createOrderDetail(@PathVariable("id") Long id,
                                         @RequestBody OrderDetailRequestDto orderDetailRequestDto) {
        return orderDetailService.createOrderDetail(orderDetailRequestDto);
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
                                             @RequestParam List<Long> selectedDetailIds) {
        orderDetailService.deleteSelectedDetails(id, selectedDetailIds);
        return "상품 삭제 성공";
    }



    // 주문 상세 내역 수정
    // 쇼핑몰을 사용하면서 주문 페이지에서 제품의 추가나 수정이 가능한 경우는 없었으며
    // 어떤 제품을 구매할 것인지 선택만 가능했다. 수정은 현재 단계에서 필요하지 않을 것으로 보인다.
//    @PutMapping("/details/{id}")
//    public OrderDetail updateOrderDetail(@PathVariable("id") Long id,
//                                    @RequestBody OrderDetailRequestDto orderDetailRequestDto) {
//
//        return orderDetailService.updateOrderDetail(id, orderDetailRequestDto);
//    }
}
