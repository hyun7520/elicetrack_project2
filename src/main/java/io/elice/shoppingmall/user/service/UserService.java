package io.elice.shoppingmall.user.service;


import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.security.Keys;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public User signUp(SignUpDto signUpDto){
        User user = User.builder()
                        .email(signUpDto.getEmail())
                        .password(signUpDto.getPassword())
                        .nickname(signUpDto.getNickname())
                        .build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }



    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("isAdmin", user.isAdmin()) // isAdmin 정보 추가
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }

}
