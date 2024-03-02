package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.ProductRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 등록
    @PostMapping
    public Product createProduct(@RequestBody ProductRequestDto productRequestDto){
        return productService.createProduct(productRequestDto);
    }

    // 수정
    @PostMapping("/{id}/update")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto productRequestDto){
        return productService.updateProduct(id, productRequestDto);
    }

    // 삭제
    @DeleteMapping("/{id}/delete") // 삭제 메서드의 URL 변경
    public ResponseEntity<String> productDelete(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been deleted.");
    }

    // 상세 페이지 조회
    @GetMapping("/{id}/delete")
    public ResponseEntity<?> getProductDetail(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found");
            //return ResponseEntity.notFound().build(); //<?>가 product일 경우
        }
    }

    // 모든 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.productList();
        return ResponseEntity.ok(products);
    }
}
