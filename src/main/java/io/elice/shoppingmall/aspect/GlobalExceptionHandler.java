package io.elice.shoppingmall.aspect;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> inputException(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> parseException(final ParseException e) {
        return ResponseEntity.badRequest().body("전달 받은 String 파싱 중 문제가 생겼습니다.");
    }
}
