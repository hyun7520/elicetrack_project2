package io.elice.shoppingmall.product.service;

import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class optionService {
    private final OptionRepository optionRepository;

    // 옵션 등록
    public void saveOption(Option option){
        optionRepository.save(option);
    }

    // 옵션 조회
    public Option getOptionById(Long id){
        Optional<Option> optionalOption = optionRepository.findById(id);
        return optionalOption.orElse(null);
    }

    // 모든 옵션 조회
    public List<Option> getAllOptions(){
        return optionRepository.findAll();
    }

    // 옵션 수정
    public void modifyOption(Option option){
        Option existingOption = getOptionById(option.getOptionId());
        if (existingOption != null) {
            // 옵션 정보 업데이트
            existingOption.setContent(option.getContent());
            existingOption.setPrice(option.getPrice());
            existingOption.setStock(option.getStock());
            optionRepository.save(existingOption);
        } else {
            throw new IllegalArgumentException("유효하지 않은 옵션 ID입니다.");
        }
    }

    // 옵션 삭제
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }
}
