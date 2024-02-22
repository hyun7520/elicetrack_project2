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

    //일부 조회
    public Product productView(Integer id){
        return productRepository.findById(id).get();
    }

    // 전체 조회
    public List<Product> ProductList(){
        return productRepository.findAll();
    }

    //수정
    public void productModify(Product product, Long id){
        Optional<Product> optionalProduct = productRepository.findById(Math.toIntExact(id));
        if (optionalProduct.isPresent()) {
            Product update = optionalProduct.get();
            update.setName(product.getName());
            update.setContent(product.getContent());
            update.setPrice(product.getPrice());
            productRepository.save(update);
        } else {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }
    }

    public void productDelete(Integer id) {
        productRepository.deleteById(id);
    }
}
