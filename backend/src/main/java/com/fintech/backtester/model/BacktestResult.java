package com.fintech.backtester.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity to persist backtest results to the H2 database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacktestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal startingBalance;
    private BigDecimal finalBalance;
    private BigDecimal totalProfit;
    private double winRate;
    private int totalTrades;
    private LocalDateTime timestamp;
}
