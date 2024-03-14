package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.product.dto.ReviewRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.repository.ProductRepository;
import io.elice.shoppingmall.product.repository.ReviewRepository;
import io.elice.shoppingmall.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    // 등록
    @Transactional
    public Review saveReview(ReviewRequestDto reviewRequestDto) {
        // 등록 전 상품 존재여부 확인
        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Review review = Review.builder()
                .context(reviewRequestDto.getContext())
                .writerNickname(reviewRequestDto.getWriterNickname())
                .createdDate(reviewRequestDto.getCreatedDate())
                .rating(reviewRequestDto.getRating())
                .product(product)
                .user(User.builder().build())
                .build();
        return reviewRepository.save(review);
    }

    // user ID로 조회
    public Page<Review> getReviewByUserId(Long userId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.reviewRepository.findByUserId(userId, pageRequest);
    }

    // 특정 상품의 리뷰 목록 조회
    public Page<Review> getReviewsByProductId(Long productId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.reviewRepository.findByProductProductId(productId, pageRequest);
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
