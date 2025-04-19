package com.crypto.server.service;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.model.PurchaseLot;
import com.crypto.server.model.Transaction;
import com.crypto.server.model.TransactionType;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.CryptoTradeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

    @Transactional
    public void buy(String token, CryptoTradeRequest request)  {
        int id = (int) jwtService.extractClaim(token, "_id");

        User user = userRepository.findById(id);
        if (user == null) throw new NotFoundException("User not found");

        BigDecimal totalCost = request.cost();
        BigDecimal amount = request.amount();
        BigDecimal profit = totalCost.negate();
        BigDecimal newBalance = user.getBalance().subtract(totalCost);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        userRepository.updateBalance(id, newBalance);

        int transactionId = transactionRepository.insert(
                user.getId(),
                TransactionType.BUY.name(),
                request.symbol(),
                amount,
                totalCost.divide(amount, 2, RoundingMode.HALF_UP),
                profit
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

    @Transactional
    public void sell(String token, CryptoTradeRequest request) {
        int id = (int) jwtService.extractClaim(token, "_id");

        BigDecimal amountToSell = request.amount();
        BigDecimal sellPricePerUnit = request.cost().divide(request.amount(), 2, RoundingMode.HALF_UP);
        BigDecimal profit = BigDecimal.ZERO;

        List<PurchaseLot> fifoLots = lotRepository.findOpenLotsFIFO(id, request.symbol());

        for (PurchaseLot lot : fifoLots) {
            if (amountToSell.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal sellAmount = lot.remainingAmount().min(amountToSell);
            profit = profit.add(calculateProfit(lot, sellAmount, sellPricePerUnit));

            lotRepository.reduceLotAmount(lot.id(), sellAmount);
            amountToSell = amountToSell.subtract(sellAmount);
        }

        userRepository.increaseBalance(id, request.cost());

        transactionRepository.insert(
               id, TransactionType.SELL.name(), request.symbol(), request.amount(), sellPricePerUnit, profit
        );
    }

    public List<Transaction> getTransactions(String token) {
        int id = (int) jwtService.extractClaim(token, "_id");
        return transactionRepository.findByUserId(id);
    }

    private BigDecimal calculateProfit(PurchaseLot lot, BigDecimal sellAmount, BigDecimal sellPricePerUnit) {
        BigDecimal costBasis = sellAmount.multiply(lot.pricePerUnit());
        BigDecimal revenue = sellAmount.multiply(sellPricePerUnit);
        return revenue.subtract(costBasis);
    }

}
