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

    // 모든 옵션 목록 조회
    @GetMapping
    public ResponseEntity<List<Option>> getAllOptions() {
        List<Option> options = optionService.getAllOptions();
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
    @PostMapping("/user/{optionId}")
    public Option updateOption(@PathVariable("id") Long id, @RequestBody OptionRequestDto optionRequestDto) {
        return optionService.updateOption(id, optionRequestDto);
    }

    // 옵션 삭제
    @DeleteMapping("/user/{optionId}")
    public Option optionDelete(@PathVariable("id") Long id){
        return optionService.deleteOption(id);
    }
}
