package io.elice.shoppingmall.user.service;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.elice.shoppingmall.user.Dto.SignUpDto;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Key;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Key key;

    @InjectMocks
    private UserService userService;

    private User user;
    private SignUpDto signUpDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setNickname("nickname");

        signUpDto = new SignUpDto();
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("password");
        signUpDto.setNickname("nickname");

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(anyString())).willReturn(user);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
    }

    @Test
    public void testSignUp() {
        // Given
        // (setUp 메서드에서 이미 설정됨)

        // When
        User createdUser = userService.signUp(signUpDto);

        // Then
        assertNotNull(createdUser);
        assertEquals(signUpDto.getEmail(), createdUser.getEmail());
        then(passwordEncoder).should().encode(signUpDto.getPassword());
    }

    @Test
    public void testAuthenticate() {
        // Given
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // When
        User authenticatedUser = userService.authenticate(signUpDto.getEmail(), signUpDto.getPassword());

        // Then
        assertNotNull(authenticatedUser);
        assertEquals(signUpDto.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Given
        signUpDto.setNickname("updatedNickname");

        // When
        User updatedUser = userService.updateUser(1L, signUpDto);

        // Then
        assertNotNull(updatedUser);
        assertEquals("updatedNickname", updatedUser.getNickname());
    }

    @Test
    public void testDeleteUser() {
        // Given

        // When
        userService.deleteUser(1L);

        // Then
        then(userRepository).should().deleteById(1L);
    }


}
