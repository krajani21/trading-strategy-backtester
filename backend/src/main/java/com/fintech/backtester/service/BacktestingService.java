package com.fintech.backtester.service;

import com.fintech.backtester.model.BacktestResult;
import com.fintech.backtester.model.PricePoint;
import com.fintech.backtester.model.TradeAction;
import com.fintech.backtester.model.TradeSignal;
import com.fintech.backtester.repository.BacktestRepository;
import com.fintech.backtester.service.strategy.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Core service that executes the backtesting simulation.
 * Iterates through price data, applies strategy signals, and simulates trades.
 */
@Service
@RequiredArgsConstructor
public class BacktestingService {

    private final Strategy strategy;
    private final BacktestRepository repository;

    /**
     * Runs a backtest simulation with the given starting balance and price data.
     *
     * @param startingBalance Initial capital
     * @param prices          Historical price data
     * @return BacktestResult with calculated metrics
     */
    public BacktestResult runBacktest(BigDecimal startingBalance, List<PricePoint> prices) {
        List<TradeSignal> signals = strategy.generateSignals(prices);

        BigDecimal cashBalance = startingBalance;
        BigDecimal holdings = BigDecimal.ZERO; // Quantity of asset held
        BigDecimal lastBuyPrice = BigDecimal.ZERO;

        int totalTrades = 0;
        int winningTrades = 0;

        for (TradeSignal signal : signals) {
            if (signal.action() == TradeAction.BUY && cashBalance.compareTo(BigDecimal.ZERO) > 0) {
                // Buy: Convert all cash to holdings
                lastBuyPrice = signal.price();
                holdings = cashBalance.divide(lastBuyPrice, 8, RoundingMode.HALF_DOWN);
                cashBalance = BigDecimal.ZERO;
                totalTrades++;

            } else if (signal.action() == TradeAction.SELL && holdings.compareTo(BigDecimal.ZERO) > 0) {
                // Sell: Convert all holdings to cash
                BigDecimal sellPrice = signal.price();
                cashBalance = holdings.multiply(sellPrice).setScale(2, RoundingMode.HALF_UP);
                holdings = BigDecimal.ZERO;
                totalTrades++;

                // Track winning trade (sold higher than bought)
                if (sellPrice.compareTo(lastBuyPrice) > 0) {
                    winningTrades++;
                }
            }
            // HOLD: Do nothing
        }

        // Calculate final portfolio value (cash + mark-to-market holdings)
        BigDecimal finalBalance = cashBalance;
        if (holdings.compareTo(BigDecimal.ZERO) > 0 && !prices.isEmpty()) {
            BigDecimal lastPrice = prices.get(prices.size() - 1).price();
            finalBalance = finalBalance.add(holdings.multiply(lastPrice)).setScale(2, RoundingMode.HALF_UP);
        }

        // Calculate win rate (based on completed round-trip trades)
        int completedTrades = totalTrades / 2; // Each BUY+SELL = 1 round-trip
        double winRate = completedTrades > 0 ? ((double) winningTrades / completedTrades) * 100 : 0.0;

        // Build and save result
        BacktestResult result = new BacktestResult();
        result.setStartingBalance(startingBalance);
        result.setFinalBalance(finalBalance);
        result.setTotalProfit(finalBalance.subtract(startingBalance));
        result.setTotalTrades(totalTrades);
        result.setWinRate(winRate);
        result.setTimestamp(LocalDateTime.now());

        return repository.save(result);
    }

    /**
     * Returns the last 10 backtest results.
     */
    public List<BacktestResult> getHistory() {
        return repository.findTop10ByOrderByTimestampDesc();
    }
}
