package io.elice.shoppingmall.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
//@NoArgsConstructor
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
    private String postcode;
    private String address1;
    private String address2;
    private String phoneNumber;
    private boolean isAdmin;
}
