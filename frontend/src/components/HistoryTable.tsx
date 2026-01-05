import React from 'react';
import { BacktestResponse } from '../types';

interface HistoryTableProps {
    history: BacktestResponse[];
    isLoading: boolean;
}

const HistoryTable: React.FC<HistoryTableProps> = ({ history, isLoading }) => {
    if (isLoading) {
        return <div className="history-loading">Loading history...</div>;
    }

    if (history.length === 0) {
        return (
            <div className="history-empty">
                <p>📋 No backtest history yet</p>
            </div>
        );
    }

    return (
        <div className="history-table">
            <h2>📋 Recent Backtests</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Starting</th>
                        <th>Final</th>
                        <th>Profit/Loss</th>
                        <th>Win Rate</th>
                        <th>Trades</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    {history.map((item) => {
                        const isProfitable = item.totalProfit > 0;
                        return (
                            <tr key={item.id}>
                                <td>#{item.id}</td>
                                <td>${item.startingBalance.toLocaleString()}</td>
                                <td>${item.finalBalance.toLocaleString()}</td>
                                <td style={{ color: isProfitable ? '#10b981' : '#ef4444' }}>
                                    {isProfitable ? '+' : ''}${item.totalProfit.toLocaleString()}
                                </td>
                                <td>{item.winRate.toFixed(1)}%</td>
                                <td>{item.totalTrades}</td>
                                <td>{new Date(item.timestamp).toLocaleDateString()}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
};

export default HistoryTable;
