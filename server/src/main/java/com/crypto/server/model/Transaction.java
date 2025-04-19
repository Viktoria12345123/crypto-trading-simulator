package com.crypto.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private int id;
    private int userId;
    private String type;
    private String coinSymbol;
    private BigDecimal amount;
    private BigDecimal pricePerUnit;
    private BigDecimal profit;
    private Instant timestamp;
}
