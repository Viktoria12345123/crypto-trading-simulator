package com.crypto.server.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record RegisterRequest(
        @Size(min = 4,message = "Username is too short!")
        String username,
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        String password,

        String rePass
) {}
