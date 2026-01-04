package com.fintech.backtester.dto;

import com.fintech.backtester.model.PricePoint;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for the backtest endpoint.
 */
@Data
public class BacktestRequest {
    private BigDecimal startingBalance;
    private List<PricePoint> prices;
}
