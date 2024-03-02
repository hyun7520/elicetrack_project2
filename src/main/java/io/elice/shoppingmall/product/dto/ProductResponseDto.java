package io.elice.shoppingmall.product.dto;

import io.elice.shoppingmall.order.model.Orders;
import io.elice.shoppingmall.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private int price;
    private String brandName;
    private String content;
    private int commentCount;
    private LocalDate createdDate;
    private String productImageUrl;
    private int deliveryPrice;
    private int averageScore;
    private int reviewCount;
    private int discountPrice;
    private Long categoryId;

    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.brandName = product.getBrandName();
        this.content = product.getContent();
        this.commentCount = product.getCommentCount();
        this.createdDate = product.getCreatedDate();
        this.productImageUrl = product.getProductImageUrl();
        this.deliveryPrice = product.getDeliveryPrice();
        this.averageScore = product.getAverageScore();
        this.reviewCount = product.getReviewCount();
        this.discountPrice = product.getDiscountPrice();
//        this.categoryId = product.getCategory_id();
    }
}
