package com.fintech.backtester.service.strategy;

import com.fintech.backtester.model.PricePoint;
import com.fintech.backtester.model.TradeAction;
import com.fintech.backtester.model.TradeSignal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Mean Reversion Strategy Implementation.
 * 
 * Logic:
 * - If price drops 5% or more from previous day -> BUY signal
 * - If price rises 5% or more from previous day -> SELL signal
 * - Otherwise -> HOLD signal
 * 
 * This is a simplified mean reversion strategy for MVP demonstration.
 */
@Component
public class MeanReversionStrategy implements Strategy {

    private static final BigDecimal THRESHOLD = new BigDecimal("0.05"); // 5%

    @Override
    public List<TradeSignal> generateSignals(List<PricePoint> prices) {
        List<TradeSignal> signals = new ArrayList<>();

        // Need at least 2 price points to calculate change
        if (prices == null || prices.size() < 2) {
            return signals;
        }

        for (int i = 1; i < prices.size(); i++) {
            PricePoint previous = prices.get(i - 1);
            PricePoint current = prices.get(i);

            BigDecimal prevPrice = previous.price();
            BigDecimal currPrice = current.price();

            // Calculate percentage change: (current - previous) / previous
            BigDecimal change = currPrice.subtract(prevPrice)
                    .divide(prevPrice, 4, RoundingMode.HALF_UP);

            TradeAction action;
            if (change.compareTo(THRESHOLD.negate()) <= 0) {
                // Price dropped 5% or more -> BUY
                action = TradeAction.BUY;
            } else if (change.compareTo(THRESHOLD) >= 0) {
                // Price rose 5% or more -> SELL
                action = TradeAction.SELL;
            } else {
                // Small movement -> HOLD
                action = TradeAction.HOLD;
            }

            signals.add(new TradeSignal(current.date(), action, currPrice));
        }

        return signals;
    }
}
