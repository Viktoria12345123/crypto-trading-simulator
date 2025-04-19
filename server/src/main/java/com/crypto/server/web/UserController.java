package com.crypto.server.web;

import com.crypto.server.model.User;
import com.crypto.server.service.JwtService;
import com.crypto.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {


    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getUserBalance(@CookieValue(name = "jwt", required = false) String token)  {

        Object id =jwtService.extractClaim(token, "_id");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserById((int) id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(Map.of("balance", user.getBalance()));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetUser(@CookieValue(name = "jwt", required = false) String token) {
        Object id = jwtService.extractClaim(token, "_id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.resetUserAccount((int) id);

        return ResponseEntity.ok(Map.of("message", "Account reset successful"));
    }


}
