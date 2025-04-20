package com.crypto.server.ServiceITest;

import com.crypto.server.model.PurchaseLot;
import com.crypto.server.model.Transaction;
import com.crypto.server.model.TransactionType;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.service.CryptoService;
import com.crypto.server.service.JwtService;
import com.crypto.server.web.dto.CryptoTradeRequest;

import com.crypto.server.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;




@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(CryptoService.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:schema.sql")
public class CryptoServiceITest{

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private JwtService jwtService;

    private User testUser;
    private String jwtToken;

    @BeforeEach
    public void setup() {

        RegisterRequest request = new RegisterRequest("testuser", "password", "password");
        testUser = userRepository.create(request);

        jwtToken = jwtService.generateToken(testUser.getId(), testUser.getUsername());
    }

    @Test
    public void testBuyCrypto() {

        CryptoTradeRequest buyRequest = new CryptoTradeRequest(new BigDecimal("0.5"), new BigDecimal("100.00"), "BTC", "Bitcoin");

        cryptoService.buy(jwtToken, buyRequest);

        User updatedUser = userRepository.findById(testUser.getId());
        assertEquals(0, updatedUser.getBalance().compareTo(new BigDecimal("9900.00")));

        List<Transaction> transactions = transactionRepository.findByUserId(testUser.getId());
        assertEquals(1, transactions.size());
        Transaction transaction = transactions.getFirst();
        assertEquals(TransactionType.BUY.name(), transaction.getType());
        assertEquals(new BigDecimal("200.00"), transaction.getPricePerUnit());


        List<PurchaseLot> purchaseLots = lotRepository.findOpenLotsFIFO(testUser.getId(), "BTC");
        assertEquals(1, purchaseLots.size());
        PurchaseLot lot = purchaseLots.getFirst();
        assertEquals(0, new BigDecimal("0.5").compareTo(lot.remainingAmount()));
        assertEquals(0, new BigDecimal("200.00").compareTo(lot.pricePerUnit()));
    }

    @Test
    public void testSellCrypto() {

        CryptoTradeRequest buyRequest = new CryptoTradeRequest(new BigDecimal("2"), new BigDecimal("100.00"), "BTC", "Bitcoin");
        cryptoService.buy(jwtToken, buyRequest);

        CryptoTradeRequest sellRequest = new CryptoTradeRequest(new BigDecimal("1"), new BigDecimal("150.00"), "BTC", "Bitcoin");

        cryptoService.sell(jwtToken, sellRequest);

        User updatedUser = userRepository.findById(testUser.getId());

        assertEquals(0, updatedUser.getBalance().compareTo(new BigDecimal("10050")));

        List<Transaction> transactions = transactionRepository.findByUserId(testUser.getId());
        assertEquals(2, transactions.size());
        Transaction transaction = transactions.getFirst();
        assertEquals(TransactionType.SELL.name(), transaction.getType());

        List<PurchaseLot> purchaseLots = lotRepository.findOpenLotsFIFO(testUser.getId(), "BTC");
        assertEquals(1, purchaseLots.size());
        PurchaseLot remainingLot = purchaseLots.getFirst();
        assertEquals(0, remainingLot.remainingAmount().compareTo(new BigDecimal("1")));

    }
}
