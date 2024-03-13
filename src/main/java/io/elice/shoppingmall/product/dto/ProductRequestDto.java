package io.elice.shoppingmall.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class ProductRequestDto {
    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @Min(value = 1000, message = "가격은 1000원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "브랜드명 필수입니다.")
    private String brandName;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String content;
    private int commentCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;
    private String productImageUrl;

    @NotNull(message = "배달 가격이 명시되어야합니다.")
    private Integer deliveryPrice;

    private int averageScore;
    private int reviewCount;
    private int discountPrice;

    @NotNull
    private Long categoryId;
}
