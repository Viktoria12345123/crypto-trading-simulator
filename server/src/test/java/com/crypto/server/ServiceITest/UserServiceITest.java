package com.crypto.server.ServiceITest;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.config.exceptions.PasswordMismatchException;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Import(UserService.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceITest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest("alice", "password", "password");
        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthResponse user = userService.register(request, response);

        assertNotNull(user);
        assertEquals("alice", user.username());

        User persistedUser = userRepository.findById(user.id()).orElseThrow(() -> new NotFoundException("User not found"));
        assertNotNull(persistedUser);
        assertEquals(user.username(), persistedUser.getUsername());
    }

    @Test
    public void testLoginUser() {
        RegisterRequest registerRequest = new RegisterRequest("bob", "securepass", "securepass");
        HttpServletResponse registerResponse = mock(HttpServletResponse.class);
        AuthResponse user = userService.register(registerRequest, registerResponse);

        LoginRequest loginRequest = new LoginRequest("bob", "securepass");
        HttpServletResponse loginResponse = mock(HttpServletResponse.class);
        AuthResponse loggedInUser = userService.login(loginRequest, loginResponse);

        assertNotNull(loggedInUser);
        assertEquals(user.username(), loggedInUser.username());
    }

    @Test
    public void testLoginUserWithWrongPassword() {
        RegisterRequest registerRequest = new RegisterRequest("carol", "password", "password");
        HttpServletResponse response = mock(HttpServletResponse.class);
        userService.register(registerRequest, response);

        LoginRequest loginRequest = new LoginRequest("carol", "wrongpassword");

        HttpServletResponse loginResponse = mock(HttpServletResponse.class);

        assertThrows(PasswordMismatchException.class, () -> userService.login(loginRequest, loginResponse));
    }

    @Test
    public void testResetUserAccount() {
        RegisterRequest registerRequest = new RegisterRequest("dave", "mypassword", "mypassword");
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthResponse user = userService.register(registerRequest, response);

        userService.resetUserAccount(user.id());

        User updatedUser = userRepository.findById(user.id()).orElseThrow(() -> new NotFoundException("User not found"));
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getBalance().compareTo(new BigDecimal("10000")));
    }
}
