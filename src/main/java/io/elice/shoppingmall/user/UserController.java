package io.elice.shoppingmall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public User register(@RequestBody User user){
        boolean isDuplicated = userService.checkEmail(user.getEmail());

        return userService.register(user);
    }

    @GetMapping("/checkEmail")
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean isDuplicated = userService.checkEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);
        return response;
    }


}
