package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/options")
public class OptionController {
    private final OptionService optionService;

    // 모든 옵션 목록 조회
    @GetMapping
    public String getAllOption(Model model){
        model.addAttribute("options", optionService.getAllOptions());
        return "options";
    }

    // 옵션 등록
    @PostMapping("/options/add")
    public String createOption(@ModelAttribute Option option){
        optionService.saveOption(option);
        return "redirect:/options";
    }

    // 옵션 수정
    @PostMapping("/modify/{id}")
    public String handleOptionModification(@ModelAttribute Option option, @PathVariable("id") Long id){
        option.setOptionId(id);
        optionService.modifyOption(option);
        return "redirect:/options";
    }


    // 옵션 삭제
    @GetMapping("/delete/{id}")
    public String optionDelete(@PathVariable("id") Long id){
        optionService.deleteOption(id);
        return "redirect:/options";
    }
}
