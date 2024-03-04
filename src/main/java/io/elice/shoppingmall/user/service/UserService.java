package io.elice.shoppingmall.user.service;


import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
   private final Key key;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Key key) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.key = key;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User signUp(SignUpDto signUpDto){
        User user = User.builder()
                        .email(signUpDto.getEmail())
                        .password(signUpDto.getPassword())
                        .nickname(signUpDto.getNickname())
                        .address1(signUpDto.getAddress1())
                        .address2(signUpDto.getAddress2())
                        .postcode(signUpDto.getPostcode())
                        .phoneNumber(signUpDto.getPhoneNumber())
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

    @Transactional
    public User updateUser(Long id, SignUpDto signUpDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + id));
        if(user.getNickname().equals(signUpDto.getNickname())){
            user.setNickname(signUpDto.getNickname());
        }
        if(signUpDto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        }
        if(user.getPostcode().equals(signUpDto.getPostcode())){
            user.setPostcode(signUpDto.getPostcode());
        }
        if(user.getAddress1().equals(signUpDto.getAddress1())){
            user.setAddress1(signUpDto.getAddress1());
        }
        if(user.getAddress2().equals(signUpDto.getAddress2())){
            user.setAddress2(signUpDto.getAddress2());
        }
        if(user.getPhoneNumber().equals(signUpDto.getPhoneNumber())){
            user.setPhoneNumber(signUpDto.getPhoneNumber());
        }
        return userRepository.save(user);
    }
}
