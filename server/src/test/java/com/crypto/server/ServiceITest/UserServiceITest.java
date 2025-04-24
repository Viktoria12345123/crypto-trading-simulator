package com.crypto.server.ServiceITest;

import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

        AuthResponse user = userService.register(request);

        assertNotNull(user);
        assertEquals("alice", user.username());

        User persistedUser = userRepository.findById(user.id());
        assertNotNull(persistedUser);
        assertEquals(user.username(), persistedUser.getUsername());
    }

    @Test
    public void testLoginUser() {

        RegisterRequest registerRequest = new RegisterRequest("bob", "securepass", "securepass");
        AuthResponse user = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("bob", "securepass");
        AuthResponse loggedInUser = userService.login(loginRequest);

        assertNotNull(loggedInUser);
        assertEquals(user.username(), loggedInUser.username());
    }

    @Test
    public void testLoginUserWithWrongPassword() {

        RegisterRequest registerRequest = new RegisterRequest("carol", "password", "password");
        AuthResponse user = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("carol", "wrongpassword");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void testResetUserAccount() {

        RegisterRequest registerRequest = new RegisterRequest("dave", "mypassword", "mypassword");
        AuthResponse user = userService.register(registerRequest);

        userService.resetUserAccount(user.id());

        User updatedUser = userRepository.findById(user.id());
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getBalance().compareTo(new BigDecimal("10000")));
    }
}
