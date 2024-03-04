package io.elice.shoppingmall.order.response;

import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.order.model.Orders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailResponse {
    public static ResponseEntity<Object> responseBuilder(OrderDetail responseObject, String message, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        response.put("order-detail", responseObject);
        response.put("message", message);
        response.put("status", httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }
}
