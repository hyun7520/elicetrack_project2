package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.dto.OptionRequestDto;
import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.entity.Review;
import io.elice.shoppingmall.product.repository.OptionRepository;
import io.elice.shoppingmall.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    // 옵션 등록
    @Transactional
    public Option createOption(OptionRequestDto optionRequestDto){
        // 등록 전 상품 존재여부 확인
        Product product = productRepository.findById(optionRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Option option = Option.builder()
                .content(optionRequestDto.getContent())
                .price(optionRequestDto.getPrice())
                .stock(optionRequestDto.getStock())
                .product(product)
                .build();
        return optionRepository.save(option);
    }

    // 특정 상품의 옵션 조회
    public List<Option> getOptionByProductId(Long productId){
        return optionRepository.findByProductProductId(productId);
    }

    // 옵션 수정
    @Transactional
    public Option updateOption(Long id, OptionRequestDto optionRequestDto){
        Optional<Option> optionalOption = optionRepository.findById(id);
        if (optionalOption.isPresent()) {
            Option existingOption = optionalOption.get();
            existingOption.updateOption(optionRequestDto);
            return optionRepository.save(existingOption);
        } else {
            throw new IllegalArgumentException("유효하지 않은 옵션 ID입니다.");
        }
    }

    // 옵션 삭제
    @Transactional
    public Option deleteOption(Long id) {
        Option deletedOption = optionRepository.findById(id).orElse(null);
        if (deletedOption != null) {
            optionRepository.delete(deletedOption);
        }
        return deletedOption;
    }
}
