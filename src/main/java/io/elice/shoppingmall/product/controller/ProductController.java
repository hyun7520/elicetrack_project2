package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 등록
    @PostMapping
    public String createProduct(Product product){
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // 수정
    @PostMapping("/{id}")
    public String updateProduct(Product product, @PathVariable("id") Long id){
        product.setProduct_id(id);
        productService.modifyProduct(product);
        return "redirect:/product-detail/" + id;
    }

    // 삭제
    @GetMapping("/{id}") // 삭제 메서드의 URL 변경
    public String productDelete(@PathVariable("id") Long id){
        productService.productDelete(id);
        return "redirect:/admin"; // 삭제 후에는 관리자 페이지로 리다이렉트
    }

    // 상세 페이지 조회
    @GetMapping("/details/{id}")
    public String deleteProduct(Model model, @PathVariable("id") Long id){
        model.addAttribute("product", productService.getProductById(id));
        return "redirect:/product-detail/" + id;
    }

    // 모든 상품 목록 조회
    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.productList());
        return "products";
    }
}
