package io.elice.shoppingmall.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.order.model.OrderDetail;
import io.elice.shoppingmall.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "productId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId; //상품코드

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; //가격

    @Column(name = "brand_name", length = 255)
    private String brandName; // 브랜드명

    @Column(nullable = false, length = 1000)
    private String content; // 내용

    @Column(name = "comment_count")
    private int commentCount; // 댓글 수

    @Column(name = "created_date")
    private LocalDate createdDate; // 생성일

    @Column(name = "product_image_url", length = 1000)
    private String productImageUrl; // 상품 이미지 URL

    @Column(name = "delivery_price")
    private int deliveryPrice; // 배송 비용

    @Column(name = "average_score")
    private int averageScore; // 평균 평점

    @Column(name = "review_count")
    private int reviewCount; // 리뷰 수

    @Column(name="discount_price")
    private int discountPrice; //할인 가격

    @OneToOne(mappedBy = "product")
    private OrderDetail orderDetail;

//    @OneToMany(mappedBy = "product")
//    private List<UserLike> userLikes;

//    @OneToMany(mappedBy = "product")
//    private List<UserScrap> userScrap;

//    @ManyToOne
//    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
//    private Category categoryid;

    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();

    @Builder
    public Product(String productName, int price, String brandName, String content, int commentCount,
                   LocalDate createdDate, String productImageUrl, int deliveryPrice, int averageScore,
                   int reviewCount, int discountPrice) {
        this.productName = productName;
        this.price = price;
        this.brandName = brandName;
        this.content = content;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        this.productImageUrl = productImageUrl;
        this.deliveryPrice = deliveryPrice;
        this.averageScore = averageScore;
        this.reviewCount = reviewCount;
        this.discountPrice = discountPrice;
    }

    public void updateProduct(ProductRequestDto productRequestDto) {
        this.productName = productRequestDto.getProductName();
        this.price = productRequestDto.getPrice();
        this.brandName = productRequestDto.getBrandName();
        this.content = productRequestDto.getContent();
        this.commentCount = productRequestDto.getCommentCount();
        this.createdDate = productRequestDto.getCreatedDate();
        this.productImageUrl = productRequestDto.getProductImageUrl();
        this.deliveryPrice = productRequestDto.getDeliveryPrice();
        this.averageScore = productRequestDto.getAverageScore();
        this.reviewCount = productRequestDto.getReviewCount();
        this.discountPrice = productRequestDto.getDiscountPrice();
    }
}
