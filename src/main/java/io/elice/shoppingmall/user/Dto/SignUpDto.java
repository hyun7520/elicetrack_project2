package io.elice.shoppingmall.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
}
