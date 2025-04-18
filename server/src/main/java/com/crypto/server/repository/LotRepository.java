package com.crypto.server.repository;

import com.crypto.server.model.Holding;
import com.crypto.server.model.PurchaseLot;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LotRepository {

    private final DataSource dataSource;

    public LotRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertLot(int userId, String symbol, BigDecimal originalAmount, BigDecimal remainingAmount, BigDecimal pricePerUnit, int transactionId) throws SQLException {
        String sql = """
            INSERT INTO purchase_lots (user_id, coin_symbol, original_amount, remaining_amount, price_per_unit, buy_transaction_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, symbol);
            stmt.setBigDecimal(3, originalAmount);
            stmt.setBigDecimal(4, remainingAmount);
            stmt.setBigDecimal(5, pricePerUnit);
            stmt.setInt(6, transactionId);

            stmt.executeUpdate();
        }
    }

    public List<PurchaseLot> findOpenLotsFIFO(int userId, String symbol) throws SQLException {
        String sql = """
            SELECT * FROM purchase_lots
            WHERE user_id = ? AND coin_symbol = ? AND remaining_amount > 0
            ORDER BY created_at ASC
        """;

        List<PurchaseLot> lots = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, symbol);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lots.add(new PurchaseLot(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("coin_symbol"),
                            rs.getBigDecimal("original_amount"),
                            rs.getBigDecimal("remaining_amount"),
                            rs.getBigDecimal("price_per_unit"),
                            rs.getInt("buy_transaction_id"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        }

        return lots;
    }

    public void reduceLotAmount(int lotId, BigDecimal amountToSubtract) throws SQLException {
        String sql = """
            UPDATE purchase_lots
            SET remaining_amount = remaining_amount - ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, amountToSubtract);
            stmt.setInt(2, lotId);

            stmt.executeUpdate();
        }
    }

    public List<Holding> findHoldingsByUserId(int userId) throws SQLException {
        String sql = """
        SELECT coin_symbol, SUM(remaining_amount) AS total_amount
        FROM purchase_lots
        WHERE user_id = ? AND remaining_amount > 0
        GROUP BY coin_symbol
    """;

        List<Holding> holdings = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    holdings.add(new Holding(
                            rs.getString("coin_symbol"),
                            rs.getBigDecimal("total_amount")
                    ));
                }
            }
        }

        return holdings;
    }

}
