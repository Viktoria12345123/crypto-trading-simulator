package com.crypto.server.repository;

import com.crypto.server.model.User;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

@Repository
public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new user in the database.
     *
     * @param request The registration request containing username and password
     * @return The created User object with generated ID
     */
    public User create(RegisterRequest request) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, request.username());
            ps.setString(2, request.password());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Creating user failed, no rows affected.");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                String username = request.username();
                return new User(id, username);
            } else {
                throw new RuntimeException("Creating user failed, no ID obtained.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Finds a user by username (used for login).
     *
     * @param request The login request containing username
     * @return The matching User object or null if not found
     */
    public User find(LoginRequest request) {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.username());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new User(id, username, password);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }


    /**
     * Retrieves a user by their ID.
     *
     * @param id The user ID
     * @return The User object or null if not found
     */
    public User findById(int id) {
        String sql = "SELECT id, username, balance FROM users WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                BigDecimal balance = rs.getBigDecimal("balance");
                return new User(userId, username, balance != null ? balance : BigDecimal.ZERO);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by ID", e);
        }
    }

    /**
     * Updates a user's balance.
     *
     * @param userId The user ID
     * @param newBalance The new balance to set
     */
    public void updateBalance(int userId, BigDecimal newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user balance", e);
        }
    }

    /**
     * Increases a user's balance by a given amount.
     *
     * @param userId The user ID
     * @param amount The amount to increase
     */
    public void increaseBalance(int userId, BigDecimal amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to increase user balance", e);
        }
    }
}
