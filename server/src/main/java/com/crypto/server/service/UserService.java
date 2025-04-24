package com.crypto.server.service;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LotRepository lotRepository;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository, LotRepository lotRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
        this.transactionRepository = transactionRepository;
    }

    public AuthResponse register(RegisterRequest request) {

        if(!Objects.equals(request.password(), request.rePass())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

         return userRepository.create(request);
    }

    public AuthResponse login(LoginRequest request)  {

        User user = userRepository.find(request);

        if(!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        return new AuthResponse(user.getId(), user.getUsername());
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void resetUserAccount(int userId)  {
        lotRepository.deleteByUserId(userId);
        transactionRepository.deleteByUserId(userId);
        userRepository.updateBalance(userId, new BigDecimal("10000"));
    }
}
