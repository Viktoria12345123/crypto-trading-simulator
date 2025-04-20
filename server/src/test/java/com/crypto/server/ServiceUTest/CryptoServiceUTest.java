package com.crypto.server.ServiceUTest;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.model.PurchaseLot;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.CryptoService;
import com.crypto.server.service.JwtService;
import com.crypto.server.web.dto.CryptoTradeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CryptoServiceUTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private LotRepository lotRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CryptoService cryptoService;

    private String token;
    private int userId;

    @BeforeEach
    public void setup() {
        token = "dummy.jwt.token";
        userId = 1;
        when(jwtService.extractClaim(token, "_id")).thenReturn(userId);
    }

    @Test
    public void testBuy_Success() {
        BigDecimal cost = new BigDecimal("1000.00");
        BigDecimal amount = new BigDecimal("0.05");

        CryptoTradeRequest request = new CryptoTradeRequest(amount, cost, "BTC", "Bitcoin");

        when(userRepository.findById(userId)).thenReturn(new User(userId, "user1", new BigDecimal("1500.00")));
        when(transactionRepository.insert(eq(userId), eq("BUY"), eq("BTC"), eq(amount), any(), any())).thenReturn(42);

        cryptoService.buy(token, request);

        verify(userRepository).updateBalance(userId, new BigDecimal("500.00"));
        verify(lotRepository).insertLot(eq(userId), eq("BTC"), eq(amount), eq(amount), any(), eq(42));
    }

    @Test
    public void testBuy_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            cryptoService.buy(token, new CryptoTradeRequest(new BigDecimal("1"), new BigDecimal("10"), "BTC", "Bitcoin"));
        });
    }

    @Test
    public void testBuy_InsufficientBalance() {
        when(userRepository.findById(userId)).thenReturn(new User(userId, "u", new BigDecimal("5")));

        assertThrows(IllegalArgumentException.class, () -> {
            cryptoService.buy(token, new CryptoTradeRequest(new BigDecimal("1"), new BigDecimal("10"), "BTC", "Bitcoin"));
        });
    }

    @Test
    public void testSell_Success() {
        BigDecimal cost = new BigDecimal("1000.00");
        BigDecimal amount = new BigDecimal("0.05");

        CryptoTradeRequest request = new CryptoTradeRequest(amount, cost, "BTC", "Bitcoin");

        List<PurchaseLot> fifoLots = new ArrayList<>();
        fifoLots.add(new PurchaseLot(1, userId, "BTC", new BigDecimal("0.1"), new BigDecimal("0.05"), new BigDecimal("950.00"), 1, null));

        when(lotRepository.findOpenLotsFIFO(userId, "BTC")).thenReturn(fifoLots);
        when(userRepository.findById(userId)).thenReturn(new User(userId, "user1", new BigDecimal("1500.00")));
        when(transactionRepository.insert(eq(userId), eq("SELL"), eq("BTC"), eq(amount), any(), any())).thenReturn(42);

        cryptoService.sell(token, request);

        verify(userRepository).increaseBalance(userId, cost);
        verify(lotRepository).reduceLotAmount(eq(1), eq(new BigDecimal("0.05")));
        verify(transactionRepository).insert(eq(userId), eq("SELL"), eq("BTC"), eq(amount), any(), any());
    }


    @Test
    public void testSell_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            cryptoService.sell(token, new CryptoTradeRequest(new BigDecimal("1"), new BigDecimal("10"), "BTC", "Bitcoin"));
        });
    }

    @Test
    public void testSell_NoLotsAvailable() {
        BigDecimal cost = new BigDecimal("1000.00");
        BigDecimal amount = new BigDecimal("0.05");

        CryptoTradeRequest request = new CryptoTradeRequest(amount, cost, "BTC", "Bitcoin");

        when(lotRepository.findOpenLotsFIFO(userId, "BTC")).thenReturn(new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(new User(userId, "user1", new BigDecimal("1500.00")));

        assertThrows(IllegalArgumentException.class, () -> {
            cryptoService.sell(token, request);
        });
    }

    @Test
    public void testCalculateProfit() {
        BigDecimal sellAmount = new BigDecimal("0.02");
        BigDecimal sellPricePerUnit = new BigDecimal("1200.00");
        BigDecimal pricePerUnit = new BigDecimal("1000.00");
        BigDecimal originalAmount = new BigDecimal("0.05");
        BigDecimal remainingAmount = new BigDecimal("0.05");

        PurchaseLot lot = new PurchaseLot(
                1,
                userId,
                "BTC",
                originalAmount,
                remainingAmount,
                pricePerUnit,
                42,
                LocalDateTime.now()
        );

        BigDecimal costBasis = sellAmount.multiply(pricePerUnit);
        BigDecimal revenue = sellAmount.multiply(sellPricePerUnit);
        BigDecimal expectedProfit = revenue.subtract(costBasis);

        when(jwtService.extractClaim(token, "_id")).thenReturn(userId);
        when(lotRepository.findOpenLotsFIFO(userId, "BTC")).thenReturn(List.of(lot));
        when(userRepository.findById(userId)).thenReturn(new User(userId, "user1", new BigDecimal("1500.00")));
        when(transactionRepository.insert(eq(userId), eq("SELL"), eq("BTC"), eq(sellAmount), any(), eq(expectedProfit)))
                .thenReturn(42);

        CryptoTradeRequest request = new CryptoTradeRequest(sellAmount, new BigDecimal("24.00"), "BTC", "Bitcoin");
        cryptoService.sell(token, request);

        verify(transactionRepository).insert(eq(userId), eq("SELL"), eq("BTC"), eq(sellAmount), any(), eq(expectedProfit));
    }


}
