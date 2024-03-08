package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.ReviewRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public Review createReview(@RequestBody Review review){
        return reviewService.saveReview(review);
    }

    // 리뷰 수정
    @PostMapping("/{id}")
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
<<<<<<< HEAD
//    @GetMapping("/product/{productId}")
//    public List<Review> reviewsByProduct(@PathVariable("productId") Long productId){
//        return reviewService.getReviewsByProductId(productId);
//    }

    // 모든 리뷰 목록 조회
    @GetMapping
    public List<Review> getAllReviews(){
        return reviewService.reviewList();
=======
    @GetMapping("/product/{productId}")
    public Page<Review> reviewsByProduct(@PathVariable("productId") Long productId, @RequestParam(value="page", defaultValue="0") int page,@RequestParam(name = "size", defaultValue = "10") int size){
        return reviewService.getReviewsByProductId(productId, page, size);
>>>>>>> 8298897327af8b074ba46e14fd88ab220e6561fc
    }

}
