package com.crypto.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseLot(int id, int userId, String coinSymbol, BigDecimal originalAmount, BigDecimal remainingAmount,
                          BigDecimal pricePerUnit, int buyTransactionId, LocalDateTime createdAt) {

}
