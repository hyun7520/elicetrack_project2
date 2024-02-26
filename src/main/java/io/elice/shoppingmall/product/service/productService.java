package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class productService {
    private final ProductRepository productRepository;

    // 등록
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    //일부 상품 조회
    public Product getProductById(Long id){
        Optional<Product> optionalProduct = productRepository.findById(Math.toIntExact(id));
        return optionalProduct.orElse(null);
    }

    // 전체 조회
    public List<Product> ProductList(){
        return productRepository.findAll();
    }

    //수정
    // 수정
    public void modifyProduct(Product product){
        Product existingProduct = getProductById(product.getProduct_id());
        if (existingProduct != null) {
            // 상품 정보 업데이트
            existingProduct.setProductName(product.getProductName());
            existingProduct.setContent(product.getContent());
            existingProduct.setPrice(product.getPrice());
            productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }
    }

    public void productDelete(Long id) {
        productRepository.deleteById(Math.toIntExact(id));
    }
}
