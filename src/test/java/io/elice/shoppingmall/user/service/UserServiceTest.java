package io.elice.shoppingmall.user.service;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.elice.shoppingmall.user.Dto.DeleteDto;
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

//@SpringBootTest
public class UserServiceTest {

/*    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Key key;

    @InjectMocks
    private UserService userService;

    private User user;
    private SignUpDto signUpDto;
    private DeleteDto deleteDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setNickname("nickname");
        user.setPostcode("12345");
        user.setAddress1("asd");
        user.setAddress2("qwe");
        user.setPhoneNumber("01012341234");

        signUpDto = new SignUpDto( "test@example.com",
                "password",
                "nickname",
                "12345",
                "asd",
                "qwe",
                "01012341234",
        false);

        deleteDto = new DeleteDto(1L, "password");

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(anyString())).willReturn(user);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    }

    @Test
    public void testGetUserById() {
        // given
        Optional<User> optionalUser = Optional.of(user);
        given(userRepository.findById(1L)).willReturn(optionalUser);

        // when
        Optional<User> foundUser = userService.getUserById(1L);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getId(), user.getId());
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
    public void testCheckEmail() {
        // given
        String email = "test@example.com";
        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        boolean exists = userService.checkEmail(email);

        // then
        assertTrue(exists);
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
    public void testCheckPassword() {
        boolean result = userService.checkPassword(deleteDto);
        assertTrue(result, "Password should match");
    }



    @Test
    public void testDeleteUser() {
        // Given

        // When
        userService.deleteUser(1L);

        // Then
        then(userRepository).should().deleteById(1L);
    }

    @Test
    public void testGetUserCount() {
        given(userRepository.count()).willReturn(10L); // 가정: 저장소에 10명의 사용자가 있음
        int count = userService.getUserCount();
        assertEquals(10, count, "The count should be 10");
    }

    @Test
    public void testGetAdminCount() {
        given(userRepository.countByIsAdmin(true)).willReturn(5); // 가정: 저장소에 5명의 관리자가 있음
        int adminCount = userService.getAdminCount();
        assertEquals(5, adminCount, "The admin count should be 5");
    }*/


}
