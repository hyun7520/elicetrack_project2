package io.elice.shoppingmall.user.controller;


import io.elice.shoppingmall.user.Dto.SignInDto;
import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.service.UserService;
import io.elice.shoppingmall.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/sign-up")
    public User signUp(@RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @GetMapping("/checkEmail")
    public Boolean checkEmail(@RequestParam String email) {

        return userService.checkEmail(email);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto) {
        User user = userService.authenticate(signInDto.getEmail(), signInDto.getPassword());
        if (user != null) {
            String jwt = userService.generateJwtToken(user);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }


}
