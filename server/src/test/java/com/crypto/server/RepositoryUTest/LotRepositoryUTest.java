package com.crypto.server.RepositoryUTest;

import com.crypto.server.model.Holding;
import com.crypto.server.model.PurchaseLot;
import com.crypto.server.repository.LotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LotRepositoryUTest {

    @Mock private DataSource dataSource;
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    @InjectMocks private LotRepository lotRepository;

    @BeforeEach
    public void setup() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testInsertLot() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        lotRepository.insertLot(1, "BTC", BigDecimal.TEN, BigDecimal.TEN, new BigDecimal("50000"), 123);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setString(2, "BTC");
        verify(preparedStatement).setBigDecimal(3, BigDecimal.TEN);
        verify(preparedStatement).setBigDecimal(4, BigDecimal.TEN);
        verify(preparedStatement).setBigDecimal(5, new BigDecimal("50000"));
        verify(preparedStatement).setInt(6, 123);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testFindOpenLotsFIFO() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("coin_symbol")).thenReturn("BTC");
        when(resultSet.getBigDecimal("original_amount")).thenReturn(BigDecimal.TEN);
        when(resultSet.getBigDecimal("remaining_amount")).thenReturn(BigDecimal.ONE);
        when(resultSet.getBigDecimal("price_per_unit")).thenReturn(new BigDecimal("50000"));
        when(resultSet.getInt("buy_transaction_id")).thenReturn(123);
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        List<PurchaseLot> lots = lotRepository.findOpenLotsFIFO(1, "BTC");

        assertEquals(1, lots.size());
        assertEquals("BTC", lots.getFirst().coinSymbol());
    }

    @Test
    public void testReduceLotAmount() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        lotRepository.reduceLotAmount(42, new BigDecimal("0.5"));

        verify(preparedStatement).setBigDecimal(1, new BigDecimal("0.5"));
        verify(preparedStatement).setInt(2, 42);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testFindHoldingsByUserId() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("coin_symbol")).thenReturn("ETH");
        when(resultSet.getBigDecimal("total_amount")).thenReturn(new BigDecimal("2.5"));

        List<Holding> holdings = lotRepository.findHoldingsByUserId(99);

        assertEquals(1, holdings.size());
        assertEquals("ETH", holdings.getFirst().symbol());
        assertEquals(new BigDecimal("2.5"), holdings.getFirst().amount());
    }

    @Test
    public void testDeleteByUserId() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        lotRepository.deleteByUserId(123);

        verify(preparedStatement).setInt(1, 123);
        verify(preparedStatement).executeUpdate();
    }
}
