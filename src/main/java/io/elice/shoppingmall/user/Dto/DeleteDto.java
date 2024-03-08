package io.elice.shoppingmall.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class DeleteDto {
    private Long id;
    private String password;
}
