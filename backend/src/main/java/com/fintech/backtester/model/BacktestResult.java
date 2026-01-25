package com.fintech.backtester.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DynamoDB Bean to persist backtest results.
 */
@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacktestResult {

    private String id;
    private BigDecimal startingBalance;
    private BigDecimal finalBalance;
    private BigDecimal totalProfit;
    private double winRate;
    private int totalTrades;
    private String timestamp; // DynamoDB handles Strings better effectively for simple sort

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    
    // We strictly need setters for the bean if we moved annotations to getters, 
    // but Lombok @Data handles fields. 
    // However, DynamoDbBean annotations usually go on getters.
    
    // Let's rely on standard getter annotation placement:
    
    public String getTimestamp() {
        return timestamp;
    }
}
