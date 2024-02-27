package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class productService {
    private final ProductRepository productRepository;

    // 등록
    @Transactional
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    //일부 상품 조회
    public Product getProductById(Long id){
        Optional<Product> optionalProduct = productRepository.findById(Math.toIntExact(id));
        return optionalProduct.orElse(null);
    }

    // 전체 조회
    public List<Product> productList(){
        return productRepository.findAll();
    }

    //수정
    @Transactional
    public void modifyProduct(Product proeduct){
        Product existingProduct = getProductById(proeduct.getProduct_id());
        if (existingProduct != null) {
            // 상품 정보 업데이트
            existingProduct.updateProduct(proeduct);
            productRepository.save(existingProduct);
        } else {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }
    }

    @Transactional
    public void productDelete(Long id) {
        productRepository.deleteById(Math.toIntExact(id));
    }
}
