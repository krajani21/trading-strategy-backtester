package com.fintech.backtester.service.strategy;

import com.fintech.backtester.model.PricePoint;
import com.fintech.backtester.model.TradeAction;
import com.fintech.backtester.model.TradeSignal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MeanReversionStrategy.
 */
class MeanReversionStrategyTest {

    private MeanReversionStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MeanReversionStrategy();
    }

    @Test
    @DisplayName("Should generate BUY signal when price drops 5% or more")
    void shouldGenerateBuySignalOnPriceDrop() {
        // Given: 100 -> 94 is a 6% drop
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("94.00"))
        );

        // When
        List<TradeSignal> signals = strategy.generateSignals(prices);

        // Then
        assertEquals(1, signals.size());
        assertEquals(TradeAction.BUY, signals.get(0).action());
        assertEquals(new BigDecimal("94.00"), signals.get(0).price());
    }

    @Test
    @DisplayName("Should generate SELL signal when price rises 5% or more")
    void shouldGenerateSellSignalOnPriceRise() {
        // Given: 100 -> 106 is a 6% rise
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("106.00"))
        );

        // When
        List<TradeSignal> signals = strategy.generateSignals(prices);

        // Then
        assertEquals(1, signals.size());
        assertEquals(TradeAction.SELL, signals.get(0).action());
    }

    @Test
    @DisplayName("Should generate HOLD signal on small price changes")
    void shouldGenerateHoldSignalOnSmallChange() {
        // Given: 100 -> 103 is only 3%
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("103.00"))
        );

        // When
        List<TradeSignal> signals = strategy.generateSignals(prices);

        // Then
        assertEquals(1, signals.size());
        assertEquals(TradeAction.HOLD, signals.get(0).action());
    }

    @Test
    @DisplayName("Should generate BUY at exactly 5% threshold")
    void shouldBuyAtExactThreshold() {
        // Given: 100 -> 95 is exactly 5% drop
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("95.00"))
        );

        // When
        List<TradeSignal> signals = strategy.generateSignals(prices);

        // Then
        assertEquals(1, signals.size());
        assertEquals(TradeAction.BUY, signals.get(0).action());
    }

    @Test
    @DisplayName("Should return empty list for null or insufficient data")
    void shouldReturnEmptyForInvalidInput() {
        assertTrue(strategy.generateSignals(null).isEmpty());
        assertTrue(strategy.generateSignals(List.of()).isEmpty());
        assertTrue(strategy.generateSignals(
                List.of(new PricePoint(LocalDate.now(), BigDecimal.TEN))
        ).isEmpty());
    }

    @Test
    @DisplayName("Should generate correct sequence for multiple days")
    void shouldGenerateMultipleSignals() {
        // Given
        List<PricePoint> prices = Arrays.asList(
                new PricePoint(LocalDate.of(2024, 1, 1), new BigDecimal("100.00")),
                new PricePoint(LocalDate.of(2024, 1, 2), new BigDecimal("93.00")),  // -7% BUY
                new PricePoint(LocalDate.of(2024, 1, 3), new BigDecimal("95.00")),  // +2% HOLD
                new PricePoint(LocalDate.of(2024, 1, 4), new BigDecimal("100.00")), // +5.26% SELL
                new PricePoint(LocalDate.of(2024, 1, 5), new BigDecimal("99.00"))   // -1% HOLD
        );

        // When
        List<TradeSignal> signals = strategy.generateSignals(prices);

        // Then
        assertEquals(4, signals.size());
        assertEquals(TradeAction.BUY, signals.get(0).action());
        assertEquals(TradeAction.HOLD, signals.get(1).action());
        assertEquals(TradeAction.SELL, signals.get(2).action());
        assertEquals(TradeAction.HOLD, signals.get(3).action());
    }
}
