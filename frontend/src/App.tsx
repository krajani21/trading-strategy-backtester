import { useState, useEffect } from 'react';
import { backtestApi } from './api/backtestApi';
import { BacktestRequest, BacktestResponse } from './types';
import ConfigForm from './components/ConfigForm';
import ResultChart from './components/ResultChart';
import HistoryTable from './components/HistoryTable';
import './App.css';

function App() {
  const [result, setResult] = useState<BacktestResponse | null>(null);
  const [history, setHistory] = useState<BacktestResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isHistoryLoading, setIsHistoryLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Load history on mount
  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      setIsHistoryLoading(true);
      const data = await backtestApi.getHistory();
      setHistory(data);
    } catch (err) {
      console.error('Failed to load history:', err);
    } finally {
      setIsHistoryLoading(false);
    }
  };

  const handleSubmit = async (request: BacktestRequest) => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await backtestApi.runBacktest(request);
      setResult(data);
      // Reload history to include new result
      await loadHistory();
    } catch (err) {
      console.error('Backtest failed:', err);
      setError('Failed to run backtest. Make sure the backend is running on port 8080.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>📈 Trading Strategy Backtester</h1>
        <p>Test your trading strategies with historical price data</p>
      </header>

      {error && (
        <div className="error-banner">
          ⚠️ {error}
        </div>
      )}

      <main className="app-main">
        <div className="left-panel">
          <ConfigForm onSubmit={handleSubmit} isLoading={isLoading} />
        </div>

        <div className="right-panel">
          <ResultChart result={result} />
        </div>
      </main>

      <section className="history-section">
        <HistoryTable history={history} isLoading={isHistoryLoading} />
      </section>

      <footer className="app-footer">
        <p>Built with React + TypeScript | Spring Boot + H2</p>
      </footer>
    </div>
  );
}

export default App;
