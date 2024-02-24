package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.entity.Option;
import io.elice.shoppingmall.product.service.optionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/options")
public class optionController {
    private final optionService optionService;

    // 모든 옵션 목록 조회
    @GetMapping
    public String optionAll(Model model){
        model.addAttribute("options", optionService.getAllOptions());
        return "options";
    }

    // 옵션 등록 페이지
    @GetMapping("/add")
    public String optionSavePage(Model model){
        model.addAttribute("option", new Option());
        return "option-add";
    }

    // 옵션 등록
    @PostMapping("/add")
    public String optionSave(@ModelAttribute Option option){
        optionService.saveOption(option);
        return "redirect:/options";
    }

    // 옵션 수정 페이지
    @GetMapping("/modify/{id}")
    public String optionModifyForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("option", optionService.getOptionById(id));
        return "option-modify";
    }

    // 옵션 수정
    @PostMapping("/modify/{id}")
    public String optionModify(@ModelAttribute Option option, @PathVariable("id") Long id){
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
