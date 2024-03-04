package io.elice.shoppingmall.order.response;

import io.elice.shoppingmall.order.model.Orders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class OrderResponse {

    public static ResponseEntity<Object> responseBuilder(Orders responseObject, String message, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        response.put("order", responseObject);
        response.put("message", message);
        response.put("status", httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }
}
