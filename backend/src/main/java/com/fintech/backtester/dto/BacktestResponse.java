package com.fintech.backtester.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for backtest results.
 */
@Data
@Builder
public class BacktestResponse {
    private Long id;
    private BigDecimal startingBalance;
    private BigDecimal finalBalance;
    private BigDecimal totalProfit;
    private double winRate;
    private int totalTrades;
    private LocalDateTime timestamp;
}
