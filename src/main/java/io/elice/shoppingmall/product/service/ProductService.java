package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.category.repository.CategoryRepository;
import io.elice.shoppingmall.product.dto.ProductRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 등록
    @Transactional
    public Product createProduct(ProductRequestDto productRequestDto) {
        Long categoryId = productRequestDto.getCategoryId();
        // 카테고리 ID를 사용하여 해당 카테고리를 데이터베이스에서 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리가 존재하지 않습니다: " + categoryId));


        LocalDateTime now = LocalDateTime.now();
        String dateNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .brandName(productRequestDto.getBrandName())
                .content(productRequestDto.getContent())
                .commentCount(productRequestDto.getCommentCount())
                .createdDate(LocalDate.parse(dateNow))
                .productImageUrl(productRequestDto.getProductImageUrl())
                .deliveryPrice(productRequestDto.getDeliveryPrice())
                .averageScore(productRequestDto.getAverageScore())
                .reviewCount(productRequestDto.getReviewCount())
                .discountPrice(productRequestDto.getDiscountPrice())
                .category(category)
                .build();
        return productRepository.save(product);
    }

    //상품id로 상세 조회
    public Product getProductById(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 제품을 찾을 수 없습니다: " + id));
    }

    // 전체 조회
    public Page<Product> getAllproductList(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productRepository.findAll(pageRequest);
    }

    // category id로 전체 조회
    public Page<Product> getProductsByCategoryId(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findByCategoryCategoryId(categoryId, pageRequest);
    }

    // 수정
    @Transactional
    public Product updateProduct(Long id, ProductRequestDto productRequestDto){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.updateProduct(productRequestDto);
            return productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }
    }

    // 삭제
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
