package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.OptionRequestDto;
import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/options")
public class OptionController {
    private final OptionService optionService;
    // 모든 옵션 목록 조회 (없어도 될듯)
    @GetMapping
    public ResponseEntity<List<Option>> getAllOptions() {
        List<Option> options = optionService.getAllOptions();
        return ResponseEntity.ok(options);
    }

    // 특정 상품의 옵션 조회
    @GetMapping("/{productId}")
    public ResponseEntity<List<Option>> getOptionsByProductId(@PathVariable("productId") Long productId) {
        List<Option> options = optionService.getOptionByProductId(productId);
        if (options.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(options);
    }

    // 옵션 등록
    @PostMapping
    public Option createOption(@RequestBody OptionRequestDto optionRequestDto){
        Long productId = optionRequestDto.getProductId();
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID가 필요합니다.");
        }
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
