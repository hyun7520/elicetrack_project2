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
public class reviewController {
    private final reviewService reviewService;

    // 리뷰 등록 페이지
    @GetMapping("/add")
    public String reviewSavePage(Model model){
        model.addAttribute("review", new Review());
        return "review-add";
    }

    // 리뷰 등록
    @PostMapping("/add")
    public String reviewSave(@ModelAttribute Review review){
        reviewService.saveReview(review);
        return "redirect:/reviews";
    }

    // 리뷰 수정 페이지
    @GetMapping("/modify/{id}")
    public String reviewModifyForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("review", reviewService.getReviewById(id));
        return "review-modify";
    }

    // 리뷰 수정
    @PostMapping("/modify/{id}")
    public String reviewModify(@ModelAttribute Review review, @PathVariable("id") Long id){
        review.setReviewId(id);
        reviewService.modifyReview(review);
        return "redirect:/reviews";
    }

    // 리뷰 삭제
    @GetMapping("/delete/{id}")
    public String reviewDelete(@PathVariable("id") Long id){
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
    public String allReviews(Model model){
        model.addAttribute("reviews", reviewService.reviewList());
        return "reviews";
    }
}
