package com.fintech.backtester.repository;

import com.fintech.backtester.model.BacktestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import jakarta.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DynamoDbRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    @Value("${TABLE_NAME}")
    private String tableName;

    private DynamoDbTable<BacktestResult> table;

    @PostConstruct
    public void init() {
        this.table = enhancedClient.table(tableName, TableSchema.fromBean(BacktestResult.class));
    }

    public BacktestResult save(BacktestResult result) {
        table.putItem(result);
        return result;
    }

    public List<BacktestResult> findTop10ByOrderByTimestampDesc() {
        // Note: For a real production app with many users, we would use a GSI or specific Partition Key.
        // For this demo, a Scan is acceptable as data volume is low.
        return table.scan().items().stream()
                .sorted(Comparator.comparing(BacktestResult::getTimestamp, 
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .collect(Collectors.toList());
    }
}
