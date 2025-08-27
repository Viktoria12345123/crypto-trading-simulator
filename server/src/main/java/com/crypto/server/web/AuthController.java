package com.crypto.server.web;

import com.crypto.server.model.User;
import com.crypto.server.service.JwtService;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request,
                                                 BindingResult bindingResult,
                                                 HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(null);
        }

        AuthResponse user = userService.register(request, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request,
                                              BindingResult bindingResult,
                                              HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        AuthResponse auth = userService.login(request, response);
        return ResponseEntity.ok(auth);
    }


}
