package com.fintech.backtester.repository;

import com.fintech.backtester.model.BacktestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for BacktestResult entity.
 * Provides CRUD operations and custom queries.
 */
@Repository
public interface BacktestRepository extends JpaRepository<BacktestResult, Long> {

    /**
     * Returns the last 10 backtest results ordered by timestamp (most recent first).
     */
    List<BacktestResult> findTop10ByOrderByTimestampDesc();
}
