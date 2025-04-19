package com.crypto.server.repository;

import com.crypto.server.model.Transaction;
import com.crypto.server.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private final DataSource dataSource;

    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserts a transaction record (BUY or SELL).
     *
     * @param userId         The user performing the transaction
     * @param type           Either "BUY" or "SELL"
     * @param symbol         Crypto symbol (e.g. BTC)
     * @param amount         How much crypto is bought/sold
     * @param pricePerUnit   Price of the crypto at the time of transaction
     * @param profit         Profit from the transaction (null for BUYs)
     * @return               The generated transaction ID
     */
    public int insert(int userId, String type, String symbol, BigDecimal amount, BigDecimal pricePerUnit, BigDecimal profit) {
        String sql = """
            INSERT INTO transactions (user_id, type, coin_symbol, amount, price_per_unit, profit)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setString(3, symbol);
            stmt.setBigDecimal(4, amount);
            stmt.setBigDecimal(5, pricePerUnit);
            stmt.setBigDecimal(6, profit);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new RuntimeException("Transaction insert failed — no ID returned.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert transaction", e);
        }
    }

    /**
     * Deletes all transactions associated with a given user.
     *
     * @param userId The user ID whose transactions should be deleted
     */
    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM transactions WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete transactions for user: " + userId, e);
        }
    }

    /**
     * Finds all transactions for a given user ordered by most recent first.
     *
     * @param userId The user ID
     * @return       List of transactions
     */
    public List<Transaction> findByUserId(int userId) {
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transaction> transactions = new ArrayList<>();
                while (rs.next()) {
                    Transaction tx = new Transaction(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("type"),
                            rs.getString("coin_symbol"),
                            rs.getBigDecimal("amount"),
                            rs.getBigDecimal("price_per_unit"),
                            rs.getBigDecimal("profit"),
                            rs.getTimestamp("created_at").toInstant()
                    );
                    transactions.add(tx);
                }
                return transactions;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch transactions for user: " + userId, e);
        }
    }
}