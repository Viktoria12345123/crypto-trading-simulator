package com.crypto.server.ServiceUTest;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock private LotRepository lotRepository;
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, lotRepository, transactionRepository);
    }

    @Test
    void testRegister_success() {
        RegisterRequest request = new RegisterRequest("user", "pass", "pass");

        AuthResponse user = new AuthResponse(1, "user");
        when(userRepository.create(request)).thenReturn(user);

        AuthResponse result = userService.register(request);
        assertEquals(user, result);
    }

    @Test
    void testRegister_passwordMismatch_throwsException() {
        RegisterRequest request = new RegisterRequest("user", "pass", "different");

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }

    @Test
    void testLogin_success() {
        LoginRequest request = new LoginRequest("user", "pass");
        User user = new User(1, "user","pass");

        when(userRepository.find(request)).thenReturn(user);

        AuthResponse result = userService.login(request);
        assertEquals(user.getUsername(), result.username());
    }

    @Test
    void testLogin_passwordMismatch_throwsException() {
        LoginRequest request = new LoginRequest("user", "wrongpass");
        User user = new User(1, "user", "pass");

        when(userRepository.find(request)).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> userService.login(request));
    }

    @Test
    void testResetUserAccount_success() {
        int userId = 1;

        userService.resetUserAccount(userId);

        verify(lotRepository).deleteByUserId(userId);
        verify(transactionRepository).deleteByUserId(userId);
        verify(userRepository).updateBalance(userId, new BigDecimal("10000"));
    }
}
