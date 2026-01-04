package com.fintech.backtester.service.strategy;

import com.fintech.backtester.model.PricePoint;
import com.fintech.backtester.model.TradeSignal;

import java.util.List;

/**
 * Strategy interface to allow for multiple trading algorithms.
 * Implementations: MeanReversionStrategy, (future: SMA, RSI, etc.)
 */
public interface Strategy {

    /**
     * Generates trading signals based on historical price data.
     *
     * @param prices List of historical price points
     * @return List of trade signals (BUY, SELL, or HOLD)
     */
    List<TradeSignal> generateSignals(List<PricePoint> prices);
}
