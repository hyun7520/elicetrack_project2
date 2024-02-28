package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.service.reviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final reviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public String createReview(@ModelAttribute Review review){
        reviewService.saveReview(review);
        return "redirect:/reviews";
    }

    // 리뷰 수정
    @PostMapping("/{id}")
    public String updateReview(@ModelAttribute Review review, @PathVariable("id") Long id){
        review.setReviewId(id);
        reviewService.modifyReview(review);
        return "redirect:/reviews";
    }

    // 리뷰 삭제
    @GetMapping("/{id}")
    public String deleteReview(@PathVariable("id") Long id){
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }

    // 특정 상품의 리뷰 목록 조회
    @GetMapping("/product/{productId}")
    public String reviewsByProduct(Model model, @PathVariable("productId") Long productId){
        model.addAttribute("reviews", reviewService.getReviewById(productId));
        return "reviews-by-product";
    }

    // 모든 리뷰 목록 조회
    @GetMapping
    public String getAllReviews(Model model){
        model.addAttribute("reviews", reviewService.reviewList());
        return "reviews";
    }
}
