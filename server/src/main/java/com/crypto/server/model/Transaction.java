package com.crypto.server.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        int id,
        int userId,
        String type,
        String coinSymbol,
        BigDecimal amount,
        BigDecimal pricePerUnit,
        BigDecimal profit,
        Instant timestamp
) {}
