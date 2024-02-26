package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.service.productService;
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
public class productController {
    private final productService productService;

    // 모두 관리자(admin)가 사용하는 기능
    // 등록 페이지
    @GetMapping("/add")
    public String productSavePage(){
        return "product-add";
    }

    // 등록
    @PostMapping("/products")
    public String productSave(Product product){
        productService.saveProduct(product);
        return "redirect:/productS";
    }

    // 수정 페이지
    @GetMapping("/products/modify/{id}") //임의의 주소
    public String productModiftFrom(@PathVariable("id") Long id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "product/modify"; //주소 미지정
    }

    // 수정
    @PostMapping("/products/modify/{id}")
    public String productModify(Product product, @PathVariable("id") Long id){
        product.setProduct_id(id);
        productService.modifyProduct(product);
        return "redirect:/product-detail/" + id;
    }

    // 삭제
    @GetMapping("product/{id}") //임의 주소
    public String productDelete(@PathVariable("id") Long id){
        productService.productDelete(id);
        return "/admin";
    }

    // 상세 페이지 조회
    @GetMapping("/product-detail/{id}")
    public String productDetail(Model model, @PathVariable("id") Long id){
        model.addAttribute("product", productService.getProductById(id));
        return "redirect:/product-detail/" + id;
    }
}
