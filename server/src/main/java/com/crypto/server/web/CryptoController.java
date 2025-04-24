package com.crypto.server.web;

import com.crypto.server.model.Holding;
import com.crypto.server.model.Transaction;
import com.crypto.server.repository.LotRepository;
import com.crypto.server.service.CryptoService;
import com.crypto.server.service.JwtService;
import com.crypto.server.web.dto.CryptoTradeRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody CryptoTradeRequest request, @CookieValue("jwt") String jwt) throws SQLException {

        cryptoService.buy(jwt, request);
        return ResponseEntity.ok("Buy successful");
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody CryptoTradeRequest request, @CookieValue("jwt") String jwt) throws SQLException {

        cryptoService.sell(jwt, request);
        return ResponseEntity.ok("Sell successful.");
    }

    @GetMapping("/holdings")
    public ResponseEntity<?> getHoldings(@CookieValue("jwt") String jwt){

        List<Holding> holdings =  cryptoService.holdings(jwt);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(@CookieValue("jwt") String jwt) {

            List<Transaction> transactions = cryptoService.getTransactions(jwt);
            return ResponseEntity.ok(transactions);
    }

}
