package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class reviewService {
    private final ReviewRepository reviewRepository;

    // 등록
    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    // 리뷰 ID로 조회
    public Review getReviewById(Long id){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview.orElse(null);
    }

    // 전체 조회
    public List<Review> reviewList(){
        return reviewRepository.findAll();
    }

    // 수정
    public void modifyReview(Review review){
        Review existingReview = getReviewById(review.getReviewId());
        if (existingReview != null) {
            // 리뷰 정보 업데이트
            existingReview.setContext(review.getContext());
            existingReview.setWriterNickname(review.getWriterNickname());
            existingReview.setCreatedDate(review.getCreatedDate());
            existingReview.setRating(review.getRating());
            existingReview.setProduct(review.getProduct());
            reviewRepository.save(existingReview);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 ID입니다.");
        }
    }

    // 삭제
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
