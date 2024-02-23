package io.elice.shoppingmall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(User user){
        return userRepository.save(user);
    }

    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }


}
