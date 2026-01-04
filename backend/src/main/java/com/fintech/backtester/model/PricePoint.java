package com.fintech.backtester.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a single price point in the historical data.
 * Uses BigDecimal for monetary precision (critical for fintech).
 */
public record PricePoint(LocalDate date, BigDecimal price) {
}
