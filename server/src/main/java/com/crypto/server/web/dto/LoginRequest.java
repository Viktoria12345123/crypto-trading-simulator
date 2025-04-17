package com.crypto.server.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Size(message = "Username is too short!")
        String username,

        @NotBlank(message = "Password cannot be empty!")
        String password
) {
}
