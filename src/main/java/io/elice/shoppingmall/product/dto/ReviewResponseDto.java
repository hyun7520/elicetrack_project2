package io.elice.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String context;
    private String writerNickname;
    private LocalDate createdDate;
    private int rating;
    private Long productId;
    private Long userId;
}
