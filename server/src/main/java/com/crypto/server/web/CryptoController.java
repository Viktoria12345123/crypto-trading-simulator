package com.crypto.server.web;

import com.crypto.server.model.Holding;
import com.crypto.server.model.PurchaseLot;
import com.crypto.server.model.User;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.repository.TransactionRepository;
import com.crypto.server.service.CryptoService;
import com.crypto.server.service.JwtService;
import com.crypto.server.web.dto.CryptoTradeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/api/crypto")
public class CryptoController {


    private final JwtService jwtService;
    private final TransactionRepository transactionRepository;
    private final LotRepository lotRepository;
    private final CryptoService cryptoService;

    public CryptoController(JwtService jwtService, TransactionRepository transactionRepository, LotRepository lotRepository, CryptoService cryptoService) {
        this.jwtService = jwtService;
        this.transactionRepository = transactionRepository;
        this.lotRepository = lotRepository;
        this.cryptoService = cryptoService;
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody CryptoTradeRequest request, @CookieValue("jwt") String jwt) throws SQLException {

        cryptoService.buy(jwt, request);

        return ResponseEntity.ok("Buy successful");
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody CryptoTradeRequest request, @CookieValue("jwt") String token) throws SQLException {
        return ResponseEntity.ok("Sell successful. Profit: $");
    }

    @GetMapping("/holdings")
    public ResponseEntity<?> getHoldings(@CookieValue("jwt") String jwt) throws SQLException {
        int userId = (int) jwtService.extractClaim(jwt, "_id");

        List<Holding> holdings = lotRepository.findHoldingsByUserId(userId);
        return ResponseEntity.ok(holdings);
    }

}
