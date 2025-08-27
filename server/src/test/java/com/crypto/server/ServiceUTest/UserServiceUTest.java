package com.crypto.server.ServiceUTest;

import com.crypto.server.config.exceptions.PasswordMismatchException;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.UserService;
import com.crypto.server.service.JwtService;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LotRepository lotRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, lotRepository, transactionRepository, jwtService);
    }

    @Test
    void testRegister_success() {
        RegisterRequest request = new RegisterRequest("user", "pass", "pass");
        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthResponse user = new AuthResponse(1, "user");
        when(userRepository.create(request)).thenReturn(user);
        when(jwtService.generateToken(user.id(), user.username())).thenReturn("mock-token");

        AuthResponse result = userService.register(request, response);

        assertEquals(user, result);
        verify(response).setHeader(eq("Set-Cookie"), contains("mock-token"));
    }

    @Test
    void testRegister_passwordMismatch_throwsException() {
        RegisterRequest request = new RegisterRequest("user", "pass", "different");
        HttpServletResponse response = mock(HttpServletResponse.class);

        assertThrows(PasswordMismatchException.class, () -> userService.register(request, response));
    }

    @Test
    void testLogin_success() {
        LoginRequest request = new LoginRequest("user", "pass");
        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = new User(1, "user", "pass");
        when(userRepository.find(request)).thenReturn(user);
        when(jwtService.generateToken(user.getId(), user.getUsername())).thenReturn("mock-token");

        AuthResponse result = userService.login(request, response);

        assertEquals(user.getUsername(), result.username());
        verify(response).setHeader(eq("Set-Cookie"), contains("mock-token"));
    }

    @Test
    void testLogin_passwordMismatch_throwsException() {
        LoginRequest request = new LoginRequest("user", "wrongpass");
        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = new User(1, "user", "pass");
        when(userRepository.find(request)).thenReturn(user);

        assertThrows(PasswordMismatchException.class, () -> userService.login(request, response));
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
