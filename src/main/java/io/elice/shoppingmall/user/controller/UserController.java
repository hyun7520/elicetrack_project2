package io.elice.shoppingmall.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.elice.shoppingmall.user.Dto.SignInDto;
import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.service.UserService;
import io.elice.shoppingmall.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Key key;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable(name = "id") Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody SignUpDto signUpDto) {
        return userService.updateUser(id, signUpDto);
    }


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
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("isAdmin", user.isAdmin());
            response.put("id", user.getId());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
}
