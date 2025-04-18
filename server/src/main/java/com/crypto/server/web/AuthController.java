package com.crypto.server.web;

import com.crypto.server.model.User;
import com.crypto.server.service.JwtService;
import com.crypto.server.service.UserService;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult, HttpServletResponse response) throws SQLException {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

       User user = userService.register(registerRequest);
       System.out.println(user);
       String token = jwtService.generateToken(user.getId(), user.getUsername());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();


        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response) throws SQLException {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        User user = userService.login(loginRequest);
        String token = jwtService.generateToken(user.getId(), user.getUsername());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(@CookieValue(name = "jwt", required = false) String token) {

        Object id =  jwtService.extractClaim(token, "_id");
        Object username =  jwtService.extractClaim(token, "username");

        if(id == null || username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", (int) id);
        userData.put("username",(String) username);

        return ResponseEntity.ok(userData);
    }



}
