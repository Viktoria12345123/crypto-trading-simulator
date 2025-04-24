package com.crypto.server.RepositoryUTest;

import com.crypto.server.config.exceptions.NotFoundException;
import com.crypto.server.model.User;
import com.crypto.server.repository.UserRepository;
import com.crypto.server.web.dto.AuthResponse;
import com.crypto.server.web.dto.LoginRequest;
import com.crypto.server.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.ThrowsException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserRepositoryUTest {

  @Mock
  private DataSource dataSource;

  @Mock
  private Connection connection;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  @InjectMocks
  private UserRepository userRepository;

  @BeforeEach
    public void setUp() throws Exception {
      when(dataSource.getConnection()).thenReturn(connection);
  }

  @Test
  public void testCreateUser() throws SQLException {

    RegisterRequest request = new RegisterRequest("testuser", "testpass","testpass");

      when(connection.prepareStatement(anyString(),eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
      when(preparedStatement.executeUpdate()).thenReturn(1);
      when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true);
      when(resultSet.getInt(1)).thenReturn(1);

      AuthResponse user = userRepository.create(request);

      assertEquals(1, user.id());
      assertEquals("testuser", user.username());

      verify(preparedStatement).setString(1,"testuser");
      verify(preparedStatement).setString(2,"testpass");
      verify(preparedStatement).executeUpdate();
      verify(preparedStatement).getGeneratedKeys();
      verify(resultSet).next();
      verify(resultSet).getInt(1);
  }

  @Test
  public void testFind() throws SQLException {
    LoginRequest request = new LoginRequest("testUsername", "testPassword");

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("id")).thenReturn(1);
    when(resultSet.getString("username")).thenReturn("testUsername");
    when(resultSet.getString("password")).thenReturn("testPassword");

    User user = userRepository.find(request);

    assertEquals(1, user.getId());
    assertEquals("testUsername", user.getUsername());
    assertEquals("testPassword", user.getPassword());

    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setString(1, "testUsername");
    verify(preparedStatement).executeQuery();
    verify(resultSet).next();
    verify(resultSet).getInt("id");
    verify(resultSet).getString("username");
    verify(resultSet).getString("password");

  }

  @Test
  public void testFindById_successful() throws SQLException {

    int testUserId = 42;

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("id")).thenReturn(testUserId);
    when(resultSet.getString("username")).thenReturn("john_doe");
    when(resultSet.getBigDecimal("balance")).thenReturn(new BigDecimal("1234.56"));

    User user = userRepository.findById(testUserId);

    assertNotNull(user);
    assertEquals(testUserId, user.getId());
    assertEquals("john_doe", user.getUsername());
    assertEquals(new BigDecimal("1234.56"), user.getBalance());

    verify(preparedStatement).setInt(1, testUserId);
    verify(preparedStatement).executeQuery();
    verify(resultSet).next();
  }

  @Test
  public void testFindById_NotFound() throws SQLException {

    int testUserId = 42;

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    assertThrows(NotFoundException.class, () -> userRepository.findById(testUserId));
  }

  @Test
  public void testUpdateBalance_successful() throws SQLException {
    int userId = 10;
    BigDecimal newBalance = new BigDecimal("5000.00");

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

    userRepository.updateBalance(userId, newBalance);

    verify(connection).prepareStatement("UPDATE users SET balance = ? WHERE id = ?");
    verify(preparedStatement).setBigDecimal(1, newBalance);
    verify(preparedStatement).setInt(2, userId);
    verify(preparedStatement).executeUpdate();
  }

  @Test
  public void testIncreaseBalance_successful() throws SQLException {
    int userId = 20;
    BigDecimal amount = new BigDecimal("250.75");

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

    userRepository.increaseBalance(userId, amount);

    verify(connection).prepareStatement("UPDATE users SET balance = balance + ? WHERE id = ?");
    verify(preparedStatement).setBigDecimal(1, amount);
    verify(preparedStatement).setInt(2, userId);
    verify(preparedStatement).executeUpdate();
  }




}
