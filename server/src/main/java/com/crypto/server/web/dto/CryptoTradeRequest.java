package com.crypto.server.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CryptoTradeRequest(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0001", inclusive = true, message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Cost is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Cost must be positive")
        BigDecimal cost,

        @NotBlank(message = "Symbol is required")
        String symbol,

        @NotBlank(message = "Name is required")
        String name
) {}
