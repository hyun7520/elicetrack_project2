package io.elice.shoppingmall.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    @NotNull(message = "리뷰 멘트는 없을 수 없습니다.")
    private String context;

    @NotNull(message = "작성자 닉네임은 존재해야 합니다.")
    private String writerNickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    @NotNull(message = "별점은 null일 수 없습니다.")
    private int rating;

    @NotNull(message = "상품id는 null일 수 없습니다.")
    private Long productId;

    @NotNull(message = "유저id는 null일 수 없습니다.")
    private Long userId;
}
