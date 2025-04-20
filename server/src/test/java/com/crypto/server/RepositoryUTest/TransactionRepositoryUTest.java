package com.crypto.server.RepositoryUTest;

import com.crypto.server.model.Transaction;
import com.crypto.server.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TransactionRepositoryUTest {

    @Mock private DataSource dataSource;
    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;

    @InjectMocks private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testInsertTransaction() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);

        int id = transactionRepository.insert(1, "BUY", "BTC", new BigDecimal("0.5"), new BigDecimal("200.00"), null);
        assertEquals(123, id);

        verify(statement).setInt(1, 1);
        verify(statement).setString(2, "BUY");
        verify(statement).setString(3, "BTC");
        verify(statement).setBigDecimal(4, new BigDecimal("0.5"));
        verify(statement).setBigDecimal(5, new BigDecimal("200.00"));
        verify(statement).setBigDecimal(6, null);
        verify(statement).executeUpdate();
    }

    @Test
    public void testDeleteByUserId() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        transactionRepository.deleteByUserId(5);
        verify(statement).setInt(1, 5);
        verify(statement).executeUpdate();
    }

    @Test
    public void testFindByUserIdReturnsList() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Only one row
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("user_id")).thenReturn(42);
        when(resultSet.getString("type")).thenReturn("BUY");
        when(resultSet.getString("coin_symbol")).thenReturn("BTC");
        when(resultSet.getBigDecimal("amount")).thenReturn(new BigDecimal("0.5"));
        when(resultSet.getBigDecimal("price_per_unit")).thenReturn(new BigDecimal("200.00"));
        when(resultSet.getBigDecimal("profit")).thenReturn(null);
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.from(Instant.now()));

        List<Transaction> transactions = transactionRepository.findByUserId(42);
        assertEquals(1, transactions.size());
        assertEquals("BUY", transactions.get(0).getType());
    }
}
