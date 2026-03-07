import { useState, useEffect } from "react";
import { currencyApi } from "../../api/currency";
import { isNumeric } from "../../utils/numberProcessing";

export function useCurrencyConverter() {
  const [amount, setAmount] = useState('');
  const [result, setResult] = useState(0);
  const [fromCurrency, setFromCurrency] = useState("twd");
  const [toCurrency, setToCurrency] = useState("jpy");
  const [timespan, setTimespan] = useState('1 週');
  const [rateHistory, setRateHistory] = useState([]);

  const fetchExchange = async () => {
    try {
      const exchangeResult = await currencyApi.getExchangeResult(fromCurrency ,toCurrency, amount);
      setResult(exchangeResult);
    } catch (error) {
      console.error("[Fetch Exchange Failed]: ", error);
    }
  }

  const fetchHistory = async () => {
    try {
      const historyData = await currencyApi.getRateHistory(fromCurrency, toCurrency, timespan);
      setRateHistory(historyData);
    } catch (error) {
      console.error("[Fetch Rate History Failed]: ", error);
    }
  }

  useEffect(() => {
    if (isNumeric(amount)) {
      const timer = setTimeout(fetchExchange, 300); // Debounce
      return () => clearTimeout(timer);
    }
  }, [amount, fromCurrency, toCurrency]);

  useEffect(() => {
    const timer = setTimeout(fetchHistory, 300);
    return () => clearTimeout(timer);
  }, [fromCurrency, toCurrency, timespan])

  const handleSwap = () => {
    setFromCurrency(toCurrency);
    setToCurrency(fromCurrency);
  };

  return {
    amount, setAmount,
    result,
    fromCurrency, setFromCurrency,
    toCurrency, setToCurrency,
    handleSwap,
    timespan, setTimespan,
    rateHistory
  };
}
