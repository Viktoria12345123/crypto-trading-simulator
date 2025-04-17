package com.crypto.server.service;

import com.crypto.server.model.User;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
    }

    public User register(RegisterRequest request) throws SQLException {

        if(!Objects.equals(request.password(), request.rePass())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

         return userRepository.create(request);
    }
}
