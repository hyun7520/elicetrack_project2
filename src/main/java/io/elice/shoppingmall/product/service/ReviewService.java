package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.dto.ReviewRequestDto;
import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.repository.ProductRepository;
import io.elice.shoppingmall.product.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 등록
    @Transactional
    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }

    // user ID로 조회
    public Review getReviewById(Long id){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview.orElse(null);
    }

    // 특정 상품의 리뷰 목록 조회
<<<<<<< HEAD
//    public List<Review> getReviewsByProductId(Long productId){
//        return reviewRepository.findReviewsByProductId(productId);
//    }

    // 전체 조회
    public List<Review> reviewList(){
        return reviewRepository.findAll();
=======
    public Page<Review> getReviewsByProductId(Long productId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.reviewRepository.findByProductProductId(productId, pageRequest);
>>>>>>> 8298897327af8b074ba46e14fd88ab220e6561fc
    }

    // 수정
    @Transactional
    public Review updateReview(Long id, ReviewRequestDto reviewRequestDto) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review existingReview = optionalReview.get();
            existingReview.updateReview(reviewRequestDto);
            return reviewRepository.save(existingReview);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 ID입니다.");
        }
    }


    // 삭제
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
