package com.crypto.server.repository;

import com.crypto.server.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

@Repository
public class TransactionRepository {

    private final DataSource dataSource;

    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserts a transaction record (BUY or SELL)
     *
     * @param userId         The user performing the transaction
     * @param type           Either "BUY" or "SELL"
     * @param symbol         Crypto symbol (e.g. BTC)
     * @param amount         How much crypto is bought/sold
     * @param pricePerUnit   Price of the crypto at the time of transaction
     * @return               The generated transaction ID
     * @throws SQLException  If something goes wrong with DB insert
     */
    public int insert(int userId, String type, String symbol, BigDecimal amount, BigDecimal pricePerUnit) throws SQLException {
        String sql = """
            INSERT INTO transactions (user_id, type, coin_symbol, amount, price_per_unit)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setString(3, symbol);
            stmt.setBigDecimal(4, amount);
            stmt.setBigDecimal(5, pricePerUnit);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Transaction insert failed — no ID returned.");
                }
            }
        }
    }
}
