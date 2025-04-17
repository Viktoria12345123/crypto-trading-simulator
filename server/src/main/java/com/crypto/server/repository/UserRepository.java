package com.crypto.server.repository;

import com.crypto.server.model.User;
import com.crypto.server.web.dto.RegisterRequest;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    int id = rs.getInt(1);
                    String username = request.username();
                    return new User(id, username);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }
}
