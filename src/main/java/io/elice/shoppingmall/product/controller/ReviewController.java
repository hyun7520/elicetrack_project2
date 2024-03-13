package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.ReviewRequestDto;
import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public Review createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto){
        return reviewService.saveReview(reviewRequestDto);
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    public Review updateReview(@PathVariable("id") Long id, @RequestBody ReviewRequestDto review){
        return reviewService.updateReview(id, review);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review with ID " + id + " has been deleted.");
    }

    // 특정 상품의 리뷰 조회
    @GetMapping("/product/{productId}")
    public Page<Review> reviewsByProduct(@PathVariable("productId") Long productId, @RequestParam(value="page", defaultValue="0") int page,@RequestParam(name = "size", defaultValue = "10") int size){
        return reviewService.getReviewsByProductId(productId, page, size);
    }

    // UserID로 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Review>> getReviewByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Review> reviewPage = reviewService.getReviewByUserId(userId, page, size);
        if (reviewPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewPage);
    }
}
