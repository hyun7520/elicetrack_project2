package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.OptionRequestDto;
import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.service.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/options")
public class OptionController {
    private final OptionService optionService;

    // 특정 상품의 옵션 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Option>> getOptionsByProductId(@PathVariable("productId") Long productId) {
        List<Option> options = optionService.getOptionByProductId(productId);
        if (options.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(options);
    }

    // 옵션 등록
    @PostMapping
    public Option createOption(@Valid @RequestBody OptionRequestDto optionRequestDto){
        return optionService.createOption(optionRequestDto);
    }

    // 옵션 수정
    @PutMapping("/{optionId}")
    public Option updateOption(@PathVariable("optionId") Long id, @RequestBody OptionRequestDto optionRequestDto) {
        return optionService.updateOption(id, optionRequestDto);
    }

    // 옵션 삭제
    @DeleteMapping("/{optionId}")
    public Option optionDelete(@PathVariable("optionId") Long id){
        return optionService.deleteOption(id);
    }
}
