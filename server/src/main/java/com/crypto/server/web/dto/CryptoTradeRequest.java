package com.crypto.server.web.dto;
import java.math.BigDecimal;

public record CryptoTradeRequest(
        BigDecimal amount,
        BigDecimal cost,
        String symbol,
        String name
) {}

