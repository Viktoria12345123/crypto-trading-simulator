package com.crypto.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PurchaseLot {

    private final int id;
    private final int userId;
    private final String coinSymbol;
    private final BigDecimal originalAmount;
    private final BigDecimal remainingAmount;
    private final BigDecimal pricePerUnit;
    private final int buyTransactionId;
    private final LocalDateTime createdAt;

}
