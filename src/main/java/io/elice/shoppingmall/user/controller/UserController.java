package io.elice.shoppingmall.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.elice.shoppingmall.user.Dto.DeleteDto;
import io.elice.shoppingmall.user.Dto.SignInDto;
import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.service.UserService;
import io.elice.shoppingmall.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:")
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

    @PatchMapping("/{id}/role")
    public User updateRole(@PathVariable Long id, @RequestBody boolean isAdmin) {
        return userService.updateRole(id, isAdmin);
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

    @PostMapping("/password-check")
    public ResponseEntity<?> passwordCheck(@RequestBody DeleteDto deleteDto) {
        boolean isPasswordCorrect = userService.checkPassword(deleteDto);
        if (!isPasswordCorrect) {
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
        }
        Map<String, String> response = new HashMap<>();
        response.put("response", "ok");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "ok");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsers(pageRequest);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count-user")
    public Integer getUserCount() {
        return userService.getUserCount();
    }

    @GetMapping("/count-admin")
    public Integer getAdminCount() {
        return userService.getAdminCount();
    }
}
