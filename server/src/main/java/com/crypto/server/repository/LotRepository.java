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

    /**
     * Inserts a new purchase lot into the database.
     *
     * @param userId          The ID of the user making the purchase
     * @param symbol          The crypto symbol (e.g., BTC)
     * @param originalAmount  The total amount of crypto purchased
     * @param remainingAmount The remaining amount available for selling
     * @param pricePerUnit    The price per unit of the crypto
     * @param transactionId   The associated buy transaction ID
     */
    public void insertLot(int userId, String symbol, BigDecimal originalAmount, BigDecimal remainingAmount, BigDecimal pricePerUnit, int transactionId) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert purchase lot", e);
        }
    }

    /**
     * Retrieves open purchase lots for a given user and symbol using FIFO order.
     *
     * @param userId The user ID
     * @param symbol The crypto symbol (e.g., BTC)
     * @return A list of open (unsold) purchase lots ordered by creation time
     */
    public List<PurchaseLot> findOpenLotsFIFO(int userId, String symbol)  {
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch open lots", e);
        }

        return lots;
    }

    /**
     * Reduces the remaining amount of a given purchase lot.
     *
     * @param lotId            The ID of the lot to update
     * @param amountToSubtract The amount of crypto sold to subtract
     */
    public void reduceLotAmount(int lotId, BigDecimal amountToSubtract)  {
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reduce lot amount", e);
        }
    }

    /**
     * Retrieves a summary of current crypto holdings for a user.
     *
     * @param userId The ID of the user
     * @return A list of holdings (symbol and total remaining amount)
     */
    public List<Holding> findHoldingsByUserId(int userId) {
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

            return holdings;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch holdings", e);
        }


    }

    /**
     * Deletes all purchase lots associated with a given user.
     * Typically used during account reset or user deletion.
     *
     * @param userId The ID of the user.
     */
    public void deleteByUserId(int userId){
        String sql = "DELETE FROM purchase_lots WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete lots for user", e);
        }
    }
}
