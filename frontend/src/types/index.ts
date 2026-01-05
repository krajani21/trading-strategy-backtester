// TypeScript types matching backend DTOs

export interface PricePoint {
  date: string; // ISO date string
  price: number;
}

export interface BacktestRequest {
  startingBalance: number;
  prices: PricePoint[];
}

export interface BacktestResponse {
  id: number;
  startingBalance: number;
  finalBalance: number;
  totalProfit: number;
  winRate: number;
  totalTrades: number;
  timestamp: string;
}

// For chart visualization
export interface PortfolioDataPoint {
  date: string;
  value: number;
}
