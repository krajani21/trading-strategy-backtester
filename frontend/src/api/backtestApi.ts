import axios from 'axios';
import { BacktestRequest, BacktestResponse } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const backtestApi = {
    /**
     * Run a backtest with the given parameters
     */
    runBacktest: async (request: BacktestRequest): Promise<BacktestResponse> => {
        const response = await api.post<BacktestResponse>('/backtest', request);
        return response.data;
    },

    /**
     * Get the last 10 backtest results
     */
    getHistory: async (): Promise<BacktestResponse[]> => {
        const response = await api.get<BacktestResponse[]>('/history');
        return response.data;
    },
};

export default api;
