import React from 'react';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    ReferenceLine,
} from 'recharts';
import { BacktestResponse } from '../types';

interface ResultChartProps {
    result: BacktestResponse | null;
}

const ResultChart: React.FC<ResultChartProps> = ({ result }) => {
    if (!result) {
        return (
            <div className="chart-placeholder">
                <p>📈 Run a backtest to see results</p>
            </div>
        );
    }

    // Create simple data points for visualization
    const chartData = [
        { name: 'Start', value: result.startingBalance },
        { name: 'End', value: result.finalBalance },
    ];

    const isProfitable = result.totalProfit > 0;
    const profitColor = isProfitable ? '#10b981' : '#ef4444';

    return (
        <div className="result-chart">
            <h2>📈 Backtest Results</h2>

            <div className="stats-grid">
                <div className="stat-card">
                    <span className="stat-label">Starting Balance</span>
                    <span className="stat-value">${result.startingBalance.toLocaleString()}</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Final Balance</span>
                    <span className="stat-value">${result.finalBalance.toLocaleString()}</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Total Profit/Loss</span>
                    <span className="stat-value" style={{ color: profitColor }}>
                        {isProfitable ? '+' : ''}${result.totalProfit.toLocaleString()}
                    </span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Win Rate</span>
                    <span className="stat-value">{result.winRate.toFixed(1)}%</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Total Trades</span>
                    <span className="stat-value">{result.totalTrades}</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Return</span>
                    <span className="stat-value" style={{ color: profitColor }}>
                        {isProfitable ? '+' : ''}{((result.totalProfit / result.startingBalance) * 100).toFixed(2)}%
                    </span>
                </div>
            </div>

            <div className="chart-container">
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={chartData}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
                        <XAxis dataKey="name" stroke="#9ca3af" />
                        <YAxis stroke="#9ca3af" />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: '#1f2937',
                                border: '1px solid #374151',
                                borderRadius: '8px',
                            }}
                        />
                        <ReferenceLine y={result.startingBalance} stroke="#6b7280" strokeDasharray="5 5" />
                        <Line
                            type="monotone"
                            dataKey="value"
                            stroke={profitColor}
                            strokeWidth={3}
                            dot={{ fill: profitColor, strokeWidth: 2, r: 6 }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default ResultChart;
