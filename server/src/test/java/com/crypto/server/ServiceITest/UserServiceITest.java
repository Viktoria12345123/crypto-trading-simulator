package com.crypto.server.ServiceITest;

import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(UserService.class)
@Transactional
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

        User user = userService.register(request);

        assertNotNull(user);
        assertEquals("alice", user.getUsername());

        User persistedUser = userRepository.findById(user.getId());
        assertNotNull(persistedUser);
        assertEquals(user.getUsername(), persistedUser.getUsername());
    }

    @Test
    public void testLoginUser() {

        RegisterRequest registerRequest = new RegisterRequest("bob", "securepass", "securepass");
        User user = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("bob", "securepass");
        User loggedInUser = userService.login(loginRequest);

        assertNotNull(loggedInUser);
        assertEquals(user.getUsername(), loggedInUser.getUsername());
    }

    @Test
    public void testLoginUserWithWrongPassword() {

        RegisterRequest registerRequest = new RegisterRequest("carol", "password", "password");
        User user = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("carol", "wrongpassword");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    public void testResetUserAccount() {
        // Given: A registered user with some transactions
        RegisterRequest registerRequest = new RegisterRequest("dave", "mypassword", "mypassword");
        User user = userService.register(registerRequest);

        // Add transactions, holdings, etc. (You can insert mock data here)

        // When: Reset user account is called
        userService.resetUserAccount(user.getId());

        // Then: Assert the user balance is reset to 10000
        User updatedUser = userRepository.findById(user.getId());
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getBalance().compareTo(new BigDecimal("10000")));
    }
}
