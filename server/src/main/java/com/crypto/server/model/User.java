package com.crypto.server.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String username;
    private BigDecimal balance;
    private String password;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(int userId, String username, BigDecimal bigDecimal) {
        this.id = userId;
        this.username = username;
        this.balance = bigDecimal;
    }
}
