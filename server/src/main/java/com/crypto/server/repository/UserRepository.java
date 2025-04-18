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

    public User create(RegisterRequest request) throws SQLException {

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, request.username());
            ps.setString(2, request.password());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

           ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    String username = request.username();
                    return new User(id, username);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }

        }
    }

    public User find(LoginRequest request) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, request.username());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                return new User(id, username);
            } else {
                return null;
            }


        }
    }

    public User findById(int id) throws SQLException {
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
        }
    }

    public void updateBalance(int userId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

}
