package com.crypto.server.service;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.config.exceptions.PasswordMismatchException;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LotRepository lotRepository;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       LotRepository lotRepository,
                       TransactionRepository transactionRepository,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
        this.transactionRepository = transactionRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        if (!Objects.equals(request.password(), request.rePass())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        AuthResponse user = userRepository.create(request);
        setJwtCookie(user, response);
        return user;
    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.find(request);

        if (!user.getPassword().equals(request.password())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        AuthResponse auth = new AuthResponse(user.getId(), user.getUsername());
        setJwtCookie(auth, response);
        return auth;
    }

    public User getUserById(int id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public void resetUserAccount(int userId) {
        lotRepository.deleteByUserId(userId);
        transactionRepository.deleteByUserId(userId);
        userRepository.updateBalance(userId, new BigDecimal("10000"));
    }

    private void setJwtCookie(AuthResponse user, HttpServletResponse response) {
        String token = jwtService.generateToken(user.id(), user.username());

        var cookie =ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}

