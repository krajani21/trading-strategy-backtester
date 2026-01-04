package com.fintech.backtester.service;

import com.fintech.backtester.model.BacktestResult;
import com.fintech.backtester.model.PricePoint;
import com.fintech.backtester.repository.BacktestRepository;
import com.fintech.backtester.service.strategy.MeanReversionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BacktestingService.
 */
@ExtendWith(MockitoExtension.class)
class BacktestingServiceTest {

    @Mock
    private BacktestRepository repository;

    private BacktestingService service;

    @BeforeEach
    void setUp() {
        MeanReversionStrategy strategy = new MeanReversionStrategy();
        service = new BacktestingService(strategy, repository);

        // Mock repository to return the entity with an ID
        when(repository.save(any(BacktestResult.class))).thenAnswer(invocation -> {
            BacktestResult result = invocation.getArgument(0);
            result.setId(1L);
            return result;
        });
    }

    @Test
    @DisplayName("Should execute BUY and SELL trades correctly")
    void shouldExecuteTrades() {
        // Given: Price drops 6% (BUY), then rises 6% (SELL)
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("94.00")),  // -6% BUY
                new PricePoint(LocalDate.of(2024, 1, 3), new BigDecimal("100.00"))  // +6.38% SELL
        );
        BigDecimal startingBalance = new BigDecimal("10000.00");

        // When
        BacktestResult result = service.runBacktest(startingBalance, prices);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalTrades()); // 1 BUY + 1 SELL
        assertTrue(result.getTotalProfit().compareTo(BigDecimal.ZERO) > 0); // Should be profitable
    }

    @Test
    @DisplayName("Should not trade when price is stable")
    void shouldHoldOnStablePrice() {
        // Given: Small fluctuations (less than 5%)
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("101.00")), // +1% HOLD
                new PricePoint(LocalDate.of(2024, 1, 3), new BigDecimal("99.00"))   // -2% HOLD
        );
        BigDecimal startingBalance = new BigDecimal("10000.00");

        // When
        BacktestResult result = service.runBacktest(startingBalance, prices);

        // Then
        assertEquals(0, result.getTotalTrades());
        assertEquals(0, result.getTotalProfit().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should calculate correct profit on buy low sell high")
    void shouldCalculateCorrectProfit() {
        // Given: Buy at 90, Sell at 100
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("90.00")),  // -10% BUY
                new PricePoint(LocalDate.of(2024, 1, 3), new BigDecimal("100.00"))  // +11.11% SELL
        );
        BigDecimal startingBalance = new BigDecimal("10000.00");

        // When
        BacktestResult result = service.runBacktest(startingBalance, prices);

        // Then
        // Buy at 90: 10000 / 90 = 111.11111111 units
        // Sell at 100: 111.11111111 * 100 = 11111.11
        assertTrue(result.getFinalBalance().compareTo(new BigDecimal("11000")) > 0);
        assertTrue(result.getTotalProfit().compareTo(new BigDecimal("1000")) > 0);
    }

    @Test
    @DisplayName("Should handle empty price list")
    void shouldHandleEmptyPrices() {
        // Given
        List<PricePoint> prices = List.of();
        BigDecimal startingBalance = new BigDecimal("10000.00");

        // When
        BacktestResult result = service.runBacktest(startingBalance, prices);

        // Then
        assertEquals(0, result.getTotalTrades());
        assertEquals(startingBalance, result.getFinalBalance());
    }

    @Test
    @DisplayName("Should calculate win rate correctly")
    void shouldCalculateWinRate() {
        // Given: Two profitable round-trips
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("90.00")),  // BUY
                new PricePoint(LocalDate.of(2024, 1, 3), new BigDecimal("100.00")), // SELL (win)
                new PricePoint(LocalDate.of(2024, 1, 4), new BigDecimal("90.00")),  // BUY
                new PricePoint(LocalDate.of(2024, 1, 5), new BigDecimal("100.00"))  // SELL (win)
        );
        BigDecimal startingBalance = new BigDecimal("10000.00");

        // When
        BacktestResult result = service.runBacktest(startingBalance, prices);

        // Then
        assertEquals(4, result.getTotalTrades()); // 2 BUYs + 2 SELLs
        assertEquals(100.0, result.getWinRate()); // 2/2 = 100%
    }
}
