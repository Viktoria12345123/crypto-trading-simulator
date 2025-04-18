package com.crypto.server.service;

import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.CryptoTradeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

@Service
public class CryptoService {

    private final JwtService jwtService;
    private final TransactionRepository transactionRepository;
    private final LotRepository lotRepository;
    private final UserRepository userRepository;

    public CryptoService(JwtService jwtService, TransactionRepository transactionRepository, LotRepository lotRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.transactionRepository = transactionRepository;
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
    }

    public void buy(String token, CryptoTradeRequest request) throws SQLException {
        int id = (int) jwtService.extractClaim(token, "_id");

        User user = userRepository.findById(id);
        if (user == null) throw new SQLException("User not found");

        BigDecimal totalCost = request.cost();
        BigDecimal amount = request.amount();

        if (user.getBalance().compareTo(totalCost) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        BigDecimal newBalance = user.getBalance().subtract(totalCost);
        userRepository.updateBalance(id, newBalance);

        int transactionId = transactionRepository.insert(
                user.getId(),
                "BUY",
                request.symbol(),
                amount,
                totalCost.divide(amount, 2, RoundingMode.HALF_UP)
        );

        lotRepository.insertLot(
                user.getId(),
                request.symbol(),
                amount,
                amount,
                totalCost.divide(amount, 2, RoundingMode.HALF_UP),
                transactionId
        );
    }

}
