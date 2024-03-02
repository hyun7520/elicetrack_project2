package io.elice.shoppingmall.product.service;

<<<<<<< HEAD
import io.elice.shoppingmall.product.dto.ProductRequestDto;
=======
>>>>>>> 7eadbbd773d3bb44d3eef6c7c43c2e01b82daa42
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 등록
    @Transactional
    public Product createProduct(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .brandName(productRequestDto.getBrandName())
                .content(productRequestDto.getContent())
                .commentCount(productRequestDto.getCommentCount())
                .createdDate(productRequestDto.getCreatedDate())
                .productImageUrl(productRequestDto.getProductImageUrl())
                .deliveryPrice(productRequestDto.getDeliveryPrice())
                .averageScore(productRequestDto.getAverageScore())
                .reviewCount(productRequestDto.getReviewCount())
                .discountPrice(productRequestDto.getDiscountPrice())
                //.categoryId(productRequestDto.getCategoryId())
                .build();
        return productRepository.save(product);
    }

    //일부 상품 조회
    public Product getProductById(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 제품을 찾을 수 없습니다: " + id));
    }

    // 전체 조회
    public List<Product> productList(){
        return productRepository.findAll();
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
