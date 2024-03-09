package io.elice.shoppingmall.order.controller;

import io.elice.shoppingmall.order.dto.OrderDetailRequestDto;
import io.elice.shoppingmall.order.dto.OrderDetailResponseDto;
import io.elice.shoppingmall.order.dto.OrderDetailUpdateDto;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // 특정 주문에 주문 상세 추가
    @PostMapping("/{id}/details")
    public ResponseEntity<OrderDetailResponseDto> addOrderDetail(@PathVariable("id") Long id,
                                                                 @RequestBody OrderDetailRequestDto orderDetailRequestDto) {

        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(id, orderDetailRequestDto);
            return ResponseEntity.ok(OrderDetailResponseDto.builder()
                    .orderDetail(orderDetail)
                    .message("상세 주문이 생성되었습니다!")
                    .httpStatus(HttpStatus.CREATED)
                    .build());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(OrderDetailResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }

    // 특정 주문의 주문 상세내역 조회
    @GetMapping("/{id}/details")
    public Page<OrderDetail> getOrderDetailsByUser(@PathVariable("id") Long id,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "3") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return orderDetailService.getOrderDetailsByOrder(id, pageRequest);
    }

    @PutMapping("/{id}/details/{detailId}")
    public ResponseEntity<OrderDetailResponseDto> updateOrderDetail(@PathVariable("id") Long id,
                                                    @PathVariable("detailId") Long detailId,
                                                    @RequestBody OrderDetailUpdateDto orderDetailUpdateDto) {

        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, detailId, orderDetailUpdateDto);
            return ResponseEntity.ok(OrderDetailResponseDto.builder()
                    .orderDetail(orderDetail)
                    .message("상세 주문이 수정되었습니다!!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(OrderDetailResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }

    // 선택한 주문에서 특정 주문 상세를 삭제
    @DeleteMapping("/{id}/details/{detailId}")
    public ResponseEntity<OrderDetailResponseDto> deleteOrderDetail(@PathVariable("id") Long orderId, @PathVariable("detailId") Long detailId) {

        try {
            String productName = orderDetailService.deleteOrderDetail(orderId, detailId);
            return ResponseEntity.ok(OrderDetailResponseDto.builder()
                    .message(productName + "에 대한 주문이 정상적으로 삭제되었습니다!!!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(OrderDetailResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }

    // 선택된 주문에 대한 상세내역 일괄 삭제
    @DeleteMapping("/{id}/details")
    public ResponseEntity<OrderDetailResponseDto> deleteSelectedOrderDetails(@PathVariable("id") Long id,
                                                             @RequestBody List<Long> selectedDetailIds) {

        try {
            String receiverName = orderDetailService.deleteSelectedDetails(id, selectedDetailIds);
            return ResponseEntity.ok(OrderDetailResponseDto.builder()
                    .message(receiverName + "님에 대한 주문이 삭제되었습니다!!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(OrderDetailResponseDto.builder()
                    .message("다음의 오류가 발생했습니다. \n" + e.getMessage())
                    .build());
        }
    }
}
