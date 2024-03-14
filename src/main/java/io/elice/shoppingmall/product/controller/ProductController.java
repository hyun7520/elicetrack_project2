package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.category.entity.Category;
import io.elice.shoppingmall.product.dto.ProductRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductRequestDto productRequestDto){
        return productService.createProduct(productRequestDto);
    }

    // 상품 정보 수정
    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody ProductRequestDto productRequestDto){
        return productService.updateProduct(id, productRequestDto);
    }

    // 상품 정보 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> productDelete(@PathVariable("productId") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been deleted.");
    }

    // 상품 상세 페이지 조회
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductDetail(@PathVariable("productId") Long id) {
        Product product = productService.getProductById(id);

        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 모든 상품 목록 조회
    @GetMapping
    public Page<Product> getAllproductList(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.getAllproductList(page, size);
    }

    //categoryId로 상품 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.getProductsByCategoryId(categoryId, page, size);
        return ResponseEntity.ok(products);
    }


    // 이미지 업로드
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadCategoryImage(@RequestParam("file") MultipartFile file) {
        try {
            // 이미지를 저장하고 이미지 URL을 반환
            String imageUrl = productService.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    // 이미지 반환
    @GetMapping("/get-image/{categoryId}")
    public String getImage(@PathVariable("productId") Long id) {
        Product product = productService.getProductById(id);
        String imgPath = product.getStoredFileName();
        log.info(imgPath);
        return "<img src=" + imgPath + ">";
    }
}
