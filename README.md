# 📈 Trading Strategy Backtester

A full-stack application for backtesting trading strategies against historical price data.

The application allows users to simulate trading performance using a Mean Reversion strategy on generated price data.

Try it out here: https://trade-pulse-ivory.vercel.app/

## 🚀 Features

- **Backtesting Engine**: Simulates trades based on configurable strategies.
- **Interactive UI**: Visualizes portfolio growth and trade signals.
- **Strategy**: Implements a standard Mean Reversion strategy (Buy/Sell at 5% thresholds).
- **History**: Persists backtest results for comparison.

## 🛠️ Tech Stack

- **Frontend**: React, TypeScript, Vite, Recharts
- **Backend**: Java 21, Spring Boot 3.4
- **Database**: H2 In-Memory Database

## 🏁 Getting Started

### Prerequisites

- Java 21+
- Node.js 18+

### 1. Run the Backend

The backend runs on port `8080`.

```bash
cd backend
./mvnw spring-boot:run
```

### 2. Run the Frontend

The frontend runs on port `5173`.

```bash
cd frontend
npm install
npm run dev
```

### 3. Usage

1. Open `http://localhost:5173`.
2. Enter a starting balance.
3. Click "Run Backtest" to generate market data and execute the strategy.
4. View the equity curve and trade history.

## 📚 API Reference

- `POST /api/backtest`: Execute a simulation.
- `GET /api/history`: Retrieve past results.
