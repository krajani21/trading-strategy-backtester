package com.fintech.backtester.controller;

import com.fintech.backtester.dto.BacktestRequest;
import com.fintech.backtester.dto.BacktestResponse;
import com.fintech.backtester.model.BacktestResult;
import com.fintech.backtester.service.BacktestingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for backtesting endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BacktestController {

    private final BacktestingService backtestingService;

    /**
     * POST /api/backtest
     * Accepts starting balance and price data, runs simulation, saves and returns result.
     */
    @PostMapping("/backtest")
    public ResponseEntity<BacktestResponse> runBacktest(@RequestBody BacktestRequest request) {
        BacktestResult result = backtestingService.runBacktest(
                request.getStartingBalance(),
                request.getPrices()
        );
        return ResponseEntity.ok(mapToResponse(result));
    }

    /**
     * GET /api/history
     * Returns the last 10 backtest results.
     */
    @GetMapping("/history")
    public ResponseEntity<List<BacktestResponse>> getHistory() {
        List<BacktestResult> history = backtestingService.getHistory();
        List<BacktestResponse> responses = history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private BacktestResponse mapToResponse(BacktestResult result) {
        return BacktestResponse.builder()
                .id(result.getId())
                .startingBalance(result.getStartingBalance())
                .finalBalance(result.getFinalBalance())
                .totalProfit(result.getTotalProfit())
                .winRate(result.getWinRate())
                .totalTrades(result.getTotalTrades())
                .timestamp(result.getTimestamp())
                .build();
    }
}
