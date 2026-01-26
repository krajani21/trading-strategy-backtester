import { useState } from 'react';
import type { BacktestRequest, PricePoint } from '../types';

interface ConfigFormProps {
    onSubmit: (request: BacktestRequest) => void;
    isLoading: boolean;
}

// Sample price data for demo
const generateSamplePrices = (): PricePoint[] => {
    const prices: PricePoint[] = [];
    let price = 100;
    const startDate = new Date('2024-01-01');

    for (let i = 0; i < 30; i++) {
        const date = new Date(startDate);
        date.setDate(date.getDate() + i);

        // Random walk with some volatility
        const change = (Math.random() - 0.5) * 12; // -6% to +6%
        price = Math.max(50, price + change);

        prices.push({
            date: date.toISOString().split('T')[0],
            price: Math.round(price * 100) / 100,
        });
    }
    return prices;
};

const ConfigForm: React.FC<ConfigFormProps> = ({ onSubmit, isLoading }) => {
    const [startingBalance, setStartingBalance] = useState<string>('10000');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const balance = parseFloat(startingBalance);
        if (isNaN(balance) || balance <= 0) {
            alert('Please enter a valid starting balance');
            return;
        }

        const request: BacktestRequest = {
            startingBalance: balance,
            prices: generateSamplePrices(),
        };
        onSubmit(request);
    };

    return (
        <div className="config-form">
            <h2>📊 Backtest Configuration</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="startingBalance">Starting Balance ($)</label>
                    <input
                        type="number"
                        id="startingBalance"
                        value={startingBalance}
                        onChange={(e) => setStartingBalance(e.target.value)}
                        min="0"
                        step="1"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Strategy</label>
                    <select disabled>
                        <option>Mean Reversion (5% threshold)</option>
                    </select>
                    <small>More strategies coming soon</small>
                </div>

                <div className="form-group">
                    <label>Price Data</label>
                    <p className="info-text">
                        30 days of simulated price data will be generated automatically
                    </p>
                </div>

                <button type="submit" disabled={isLoading}>
                    {isLoading ? '⏳ Running...' : '🚀 Run Backtest'}
                </button>
            </form>
        </div>
    );
};

export default ConfigForm;
